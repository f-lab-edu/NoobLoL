package com.nooblol.community.service;

import com.nooblol.community.dto.UserInfoUpdateDto;
import com.nooblol.global.dto.ResponseDto;

public interface UserInfoService {

  /**
   * 사용자 정보 변경에 대한 기능으로 닉네임과 Password에 대해서만 변경이 가능하다.
   *
   * @param userUpdateInfoDto
   * @return
   */
  ResponseDto updateUserInfo(UserInfoUpdateDto userUpdateInfoDto);
}
