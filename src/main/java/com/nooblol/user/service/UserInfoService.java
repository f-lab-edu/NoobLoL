package com.nooblol.user.service;

import com.nooblol.user.dto.UserInfoUpdateDto;
import com.nooblol.user.dto.UserLoginDto;
import com.nooblol.global.dto.ResponseDto;
import javax.servlet.http.HttpSession;

public interface UserInfoService {

  /**
   * 사용자 정보 변경에 대한 기능으로 닉네임과 Password에 대해서만 변경이 가능하다.
   *
   * @param userUpdateInfoDto
   * @return
   */
  ResponseDto updateUserInfo(UserInfoUpdateDto userUpdateInfoDto);

  ResponseDto userLogin(UserLoginDto userLoginDto, HttpSession session);

  ResponseDto userLogout(HttpSession session);
}
