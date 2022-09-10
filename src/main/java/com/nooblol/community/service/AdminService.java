package com.nooblol.community.service;

import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.global.dto.ResponseDto;
import javax.servlet.http.HttpSession;

public interface AdminService {

  ResponseDto addAdminMember(UserSignUpRequestDto userSignUpRequestDto, HttpSession session);

  ResponseDto deleteAdminMember(UserSignOutDto userSignOutDto, HttpSession session);
}
