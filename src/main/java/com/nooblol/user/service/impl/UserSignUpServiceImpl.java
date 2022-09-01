package com.nooblol.user.service.impl;

import com.nooblol.user.dto.UserSignUpRequestDto;
import com.nooblol.user.mapper.UserSignUpMapper;
import com.nooblol.user.service.UserSendMailService;
import com.nooblol.user.service.UserSignUpService;
import com.nooblol.user.utils.UserRoleStatus;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.UserUtils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserSignUpServiceImpl implements UserSignUpService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserSignUpMapper userSignUpMapper;
  private final UserSendMailService userSendMailService;
  private final Environment environment;

  @Override
  public ResponseDto signUpUser(UserSignUpRequestDto userDto) {
    try {
      String encodePassword = UserUtils.stringChangeToSha512(userDto.getPassword());
      userDto.setPassword(encodePassword);

      userSignUpMapper.insertSignUpUser(userDto);

    } catch (Exception e) {
      if (e instanceof DuplicateKeyException) {
        SQLException se = (SQLException) e.getCause();
        if (se.getErrorCode() == 23505) {
          throw new IllegalArgumentException(ExceptionMessage.HAVE_DATA);
        }
      }
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }

    boolean result = sendSignUpUserMail(userDto);

    ResponseDto response = ResponseEnum.OK.getResponse();
    response.setResult(result);
    return response;
  }

  @Override
  public boolean sendSignUpUserMail(UserSignUpRequestDto userDto) {
    return userSendMailService.sendMail(
        userDto.getUserEmail(),
        getAuthMailTitle(userDto)
    );
  }

  @Override
  public ResponseDto reSendSignUpUserMail(String userEmail) {
    UserSignUpRequestDto userDto = userSignUpMapper.selectUserInfoByEmail(userEmail);

    if (ObjectUtils.isEmpty(userDto)) {
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }

    if (userDto.getUserRole() != UserRoleStatus.UNAUTH_USER.getRoleValue()) {
      throw new IllegalArgumentException(
          userDto.getUserName() + "님의 계정은 활성화가 필요한 상태가 아닙니다."
      );
    }

    boolean result = sendSignUpUserMail(userDto);

    ResponseDto response = ResponseEnum.OK.getResponse();
    response.setResult(result);
    return response;
  }

  @Override
  public ResponseDto changeRoleAuthUser(String userId) {
    UserSignUpRequestDto dbUserData = userSignUpMapper.selectUserInfoByUserId(userId);
    if (ObjectUtils.isEmpty(dbUserData)) {
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }

    if (dbUserData.getUserRole() != UserRoleStatus.UNAUTH_USER.getRoleValue()) {
      throw new IllegalArgumentException(
          dbUserData.getUserName() + "님의 계정은 활성화가 필요한 상태가 아닙니다."
      );
    }

    UserSignUpRequestDto userDto = new UserSignUpRequestDto();
    userDto.setUserId(userId);
    userDto.setUserRole(UserRoleStatus.AUTH_USER.getRoleValue());

    try {
      int updateResult = userSignUpMapper.updateUserRole(userDto);
      ResponseDto response = ResponseEnum.OK.getResponse();

      if (updateResult <= 0) {
        response.setResult(false);
      } else {
        response.setResult(true);
      }

      return response;
    } catch (Exception e) {
      log.error("User Role Update Error!!" + userId);
      throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR);
    }
  }

  @Override
  public Map<String, String> getAuthMailTitle(UserSignUpRequestDto userDto) {
    Map<String, String> mailContent = new HashMap<>();

    String titleStr = "[NoobLoL]" + userDto.getUserName() + "님 회원가입 인증 메일입니다";
    mailContent.put("title", titleStr);
    try {
      mailContent.put("content", getContent(userDto));
    } catch (UnknownHostException ex) {
      log.error("AuthMail Content Make Fail");
    } catch (Exception e) {
      log.error("AuthMail Map Make Fail");
    } finally {
      //Content가 Null이어도 Validation을 Mail에서 진행하니, 오류가 나더라도 무조건 Return을 진행해야 함.
      return mailContent;
    }
  }

  /*
    TODO : [22. 08. 25] 메일을 확인해보니 a태그가 활성화가 되어있질 않음. html로 만들어서 제작하는 방법을 알아보는게 필요하다 판단됨.
   */
  private String getContent(UserSignUpRequestDto userDto) throws UnknownHostException {
    String domain = InetAddress.getLocalHost().getHostName();

    String[] activeProfilesAry = environment.getActiveProfiles();
    String portNum = environment.getProperty("local.server.port");
    String contentStr = "<a href=\"http://" + domain;

    if (!ObjectUtils.isEmpty(activeProfilesAry)) {
      for (String activeProfile : activeProfilesAry) {
        if ("local".equals(activeProfile) || "dev".equals(activeProfile)) {
          contentStr += ":" + portNum + "";
          break;
        }
      }
    }
    contentStr += "/user/auth/" + userDto.getUserId() + "\"> NoobLoL 회원인증 링크 입니다 </a>";

    return contentStr;
  }

}
