package com.nooblol.community.service;

import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.global.dto.ResponseDto;

public interface AdminService {

  ResponseDto addAdminMember(UserSignUpRequestDto userSignUpRequestDto);

  ResponseDto deleteAdminMember(UserSignOutDto userSignOutDto);
}
