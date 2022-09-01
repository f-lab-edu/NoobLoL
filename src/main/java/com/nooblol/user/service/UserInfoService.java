package com.nooblol.user.service;

import com.nooblol.user.dto.UserInfoUpdateDto;
import com.nooblol.user.dto.UserLoginDto;
import com.nooblol.global.dto.ResponseDto;
import javax.servlet.http.HttpServletRequest;

public interface UserInfoService {

  ResponseDto updateUserInfo(UserInfoUpdateDto userUpdateInfoDto);

  ResponseDto userLogin(UserLoginDto userLoginDto, HttpServletRequest request);

  ResponseDto userLogout(HttpServletRequest request);
}
