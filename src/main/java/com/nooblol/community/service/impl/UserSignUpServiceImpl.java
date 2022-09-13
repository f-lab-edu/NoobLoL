package com.nooblol.community.service.impl;

import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.mapper.UserSignUpMapper;
import com.nooblol.community.service.UserSendMailService;
import com.nooblol.community.service.UserSignUpService;
import com.nooblol.community.utils.UserRoleStatus;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.MailConstants;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.EncryptUtils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSignUpServiceImpl implements UserSignUpService {

  private final UserSignUpMapper userSignUpMapper;
  private final UserSendMailService userSendMailService;
  private final Environment environment;

  @Override
  public ResponseDto signUpUser(UserSignUpRequestDto userDto) {
    try {
      String encodePassword = EncryptUtils.stringChangeToSha512(userDto.getPassword());
      userDto.setPassword(encodePassword);
      userSignUpMapper.insertSignUpUser(userDto);

    } catch (DuplicateKeyException e) {
      throw new IllegalArgumentException(ExceptionMessage.HAVE_DATA, e);
    } catch (Exception e) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST, e);
    }

    ResponseDto response = ResponseEnum.OK.getResponse();
    response.setResult(sendSignUpUserMail(userDto));
    return response;
  }

  @Override
  public boolean sendSignUpUserMail(UserSignUpRequestDto userDto) {
    return userSendMailService.sendMail(
        userDto.getUserEmail(),
        getAuthMailContent(userDto)
    );
  }

  @Override
  public ResponseDto reSendSignUpUserMail(String userEmail) {
    UserSignUpRequestDto userDto = userSignUpMapper.selectUserInfoByEmail(userEmail);

    if (ObjectUtils.isEmpty(userDto)) {
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }

    isNotUnAuthUser(userDto);

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

    isNotUnAuthUser(dbUserData);

    UserSignUpRequestDto userDto = new UserSignUpRequestDto();
    userDto.setUserId(userId);
    userDto.setUserRole(UserRoleStatus.AUTH_USER.getRoleValue());

    try {

      ResponseDto response = ResponseEnum.OK.getResponse();
      response.setResult(userSignUpMapper.updateUserRole(userDto) > 0);
      return response;

    } catch (Exception e) {
      throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR, e);
    }
  }

  @Override
  public Map<String, String> getAuthMailContent(UserSignUpRequestDto userDto) {
    Map<String, String> mailContent = new HashMap<>();

    String titleStr = "[NoobLoL]" + userDto.getUserName() + "님 회원가입 인증 메일입니다";
    mailContent.put("title", titleStr);
    mailContent.put("name", userDto.getUserName());
    mailContent.put("context", MailConstants.USER_SIGNUP);
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

    String contentStr = "http://" + domain;

    if (!ObjectUtils.isEmpty(activeProfilesAry)) {
      for (String activeProfile : activeProfilesAry) {
        if ("local".equals(activeProfile) || "dev".equals(activeProfile)) {
          contentStr += ":" + portNum + "";
          break;
        }
      }
    }
    contentStr += "/user/auth/" + userDto.getUserId();

    return contentStr;
  }

  private void isNotUnAuthUser(UserSignUpRequestDto user) {
    if (user.getUserRole() != UserRoleStatus.UNAUTH_USER.getRoleValue()) {
      throw new IllegalArgumentException(
          user.getUserName() + "님의 계정은 활성화가 필요한 상태가 아닙니다."
      );
    }
  }
}
