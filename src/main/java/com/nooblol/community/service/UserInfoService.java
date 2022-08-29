package com.nooblol.community.service;

import com.nooblol.community.dto.UserInfoUpdateDto;
import com.nooblol.global.dto.ResponseDto;

public interface UserInfoService {

  ResponseDto updateUserInfo(UserInfoUpdateDto userUpdateInfoDto);
}
