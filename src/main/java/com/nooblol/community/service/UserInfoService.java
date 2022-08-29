package com.nooblol.community.service;

import com.nooblol.community.dto.UserInfoUpdateDto;
import com.nooblol.community.dto.UserLoginDto;
import com.nooblol.global.dto.ResponseDto;
import javax.servlet.http.HttpServletRequest;

public interface UserInfoService {

  ResponseDto updateUserInfo(UserInfoUpdateDto userUpdateInfoDto);

  ResponseDto userLogin(UserLoginDto userLoginDto, HttpServletRequest request);

  ResponseDto userLogout(HttpServletRequest request);
}
