package com.nooblol.user.service.impl;

import com.nooblol.user.dto.UserDto;
import com.nooblol.user.dto.UserInfoUpdateDto;
import com.nooblol.user.dto.UserLoginDto;
import com.nooblol.user.mapper.UserInfoMapper;
import com.nooblol.user.service.UserInfoService;
import com.nooblol.user.utils.UserRoleStatus;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.SessionEnum;
import com.nooblol.global.utils.EncryptUtils;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

  private final UserInfoMapper userInfoMapper;

  @Override
  public ResponseDto updateUserInfo(UserInfoUpdateDto userInfoUpdateDto) {
    //두개가 모두 공백인 경우는 Update를 진행할 정보가 없기 떄문에 BadRequest를 반환한다.
    if (StringUtils.isBlank(userInfoUpdateDto.getNewUserName()) &&
        StringUtils.isBlank(userInfoUpdateDto.getNewPassword())
    ) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }

    try {
      userInfoUpdateDto.setOrgPassword(
          EncryptUtils.stringChangeToSha512(userInfoUpdateDto.getOrgPassword())
      );

      setNewPassword(userInfoUpdateDto);
      setNewUserName(userInfoUpdateDto);

      ResponseDto result = ResponseEnum.OK.getResponse();
      result.setResult(userInfoMapper.updateUserInfo(userInfoUpdateDto) > 0);
      return result;
    } catch (Exception e) {
      throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR, e);
    }
  }

  private void setNewPassword(UserInfoUpdateDto userInfoUpdateDto)
      throws UnsupportedEncodingException, NoSuchAlgorithmException {
    if (StringUtils.isBlank(userInfoUpdateDto.getNewPassword())) {
      userInfoUpdateDto.setNewPassword(userInfoUpdateDto.getOrgPassword());
      return;
    }
    userInfoUpdateDto.setNewPassword(
        EncryptUtils.stringChangeToSha512(userInfoUpdateDto.getNewPassword())
    );
  }

  private void setNewUserName(UserInfoUpdateDto userInfoUpdateDto) {
    if (StringUtils.isBlank(userInfoUpdateDto.getNewUserName())) {
      userInfoUpdateDto.setNewUserName(userInfoUpdateDto.getOrgUserName());
    }
  }

  @Override
  public ResponseDto userLogin(UserLoginDto userLoginDto, HttpSession session) {
    try {
      String password = EncryptUtils.stringChangeToSha512(userLoginDto.getUserPassword());
      userLoginDto.setUserPassword(password);
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR);
    }

    UserDto loginUser = userInfoMapper.selectUser(userLoginDto);

    if (ObjectUtils.isEmpty(loginUser)) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }

    ResponseDto result = ResponseEnum.OK.getResponse();
    if (loginUser.getUserRole() == UserRoleStatus.SUSPENSION_USER.getRoleValue()) {
      result.setResult(UserRoleStatus.SUSPENSION_USER);
      return result;
    }

    if (loginUser.getUserRole() == UserRoleStatus.UNAUTH_USER.getRoleValue()) {
      result.setResult(UserRoleStatus.UNAUTH_USER);
      return result;
    }

    session.setAttribute(SessionEnum.USER_LOGIN.getValue(), loginUser);
    result.setResult(loginUser);
    return result;
  }

  @Override
  public ResponseDto userLogout(HttpSession session) {
    if (session != null) {
      session.removeAttribute(SessionEnum.USER_LOGIN.getValue());
    }
    ResponseDto result = ResponseEnum.OK.getResponse();
    result.setResult(true);
    return result;
  }

  @Override
  public UserDto selectUserInfoByUserId(String userId) {
    return userInfoMapper.selectUserByUserId(userId);
  }

}

