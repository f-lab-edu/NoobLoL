package com.nooblol.community.service.impl;

import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.mapper.UserSignUpMapper;
import com.nooblol.community.service.UserSendMailService;
import com.nooblol.community.service.UserSignUpService;
import com.nooblol.community.utils.UserRoleStatus;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.UserUtils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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
      String encodePassword = UserUtils.stringChangeToSha512(userDto.getUserPassword());
      userDto.setUserPassword(encodePassword);

      if (userSignUpMapper.insertSignUpUser(userDto) <= 0) {
        return ResponseEnum.BAD_REQUEST.getResponse();
      }
    } catch (Exception e) {
      log.error("SignUpUser Db Insert Error");
      return ResponseEnum.BAD_REQUEST.getResponse();
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
    boolean result = sendSignUpUserMail(userDto);

    ResponseDto response = ResponseEnum.OK.getResponse();
    response.setResult(result);
    return response;
  }

  @Override
  public ResponseDto changeRoleAuthUser(String userId) {
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
      return ResponseEnum.INTERNAL_SERVER_ERROR.getResponse();
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

  private String getContent(UserSignUpRequestDto userDto) throws UnknownHostException {
    String domain = InetAddress.getLocalHost().getHostName();

    String[] activeProfilesAry = environment.getActiveProfiles();
    String portNum = environment.getProperty("local.server.port");
    String contentStr = "<a href='http://" + domain;
    for (String activeProfile : activeProfilesAry) {
      if ("local".equals(activeProfile) || "dev".equals(activeProfile)) {
        contentStr += ":" + portNum + "";
        break;
      }
      if ("prod".equals(activeProfile)) {
        break;
      }
    }
    contentStr += "/user/auth/" + userDto.getUserId() + "'>NoobLoL 회원인증 링크 입니다</a>";

    return contentStr;
  }

}
