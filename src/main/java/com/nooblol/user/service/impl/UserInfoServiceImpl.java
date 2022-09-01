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
import com.nooblol.global.utils.UserUtils;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserInfoMapper userInfoMapper;

  /**
   * 사용자 정보 변경에 대한 기능으로 닉네임과 Password에 대해서만 변경이 가능하다.
   *
   * @param userUpdateInfoDto
   * @return
   */
  @Override
  public ResponseDto updateUserInfo(UserInfoUpdateDto userUpdateInfoDto) {
    //두개가 모두 공백인 경우는 Update를 진행할 정보가 없기 떄문에 BadRequest를 반환한다.
    if (StringUtils.isBlank(userUpdateInfoDto.getNewUserName()) &&
        StringUtils.isBlank(userUpdateInfoDto.getNewPassword())
    ) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }

    try {
      userUpdateInfoDto.setOrgPassword(
          UserUtils.stringChangeToSha512(userUpdateInfoDto.getOrgPassword())
      );

      //New Password의 경우 null이 존재 할 수 있다. 쿼리문에서 분기처리가 존재하기에 null이 와도 상관 없다.
      userUpdateInfoDto.setNewPassword(
          UserUtils.stringChangeToSha512(userUpdateInfoDto.getNewPassword())
      );

      ResponseDto result = ResponseEnum.OK.getResponse();
      if (userInfoMapper.updateUserInfo(userUpdateInfoDto) == 0) {
        result.setResult(false);
      } else {
        result.setResult(true);
      }
      return result;
    } catch (Exception e) {
      if (e instanceof SQLException) {
        throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR);
      }
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }
  }

  @Override
  public ResponseDto userLogin(UserLoginDto userLoginDto, HttpServletRequest request) {
    try {
      String password = UserUtils.stringChangeToSha512(userLoginDto.getUserPassword());
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
      result.setResult("SUSPENSION_USER");
      return result;
    }

    if (loginUser.getUserRole() == UserRoleStatus.UNAUTH_USER.getRoleValue()) {
      result.setResult("UNAUTH_USER");
      return result;
    }

    HttpSession session = request.getSession();
    session.setAttribute(SessionEnum.USER_LOGIN.getValue(), loginUser);
    result.setResult(loginUser);
    return result;
  }

  @Override
  public ResponseDto userLogout(HttpServletRequest request) {
    HttpSession session = request.getSession();
    if (session != null) {
      session.invalidate();
    }
    return ResponseEnum.OK.getResponse();
  }

}

