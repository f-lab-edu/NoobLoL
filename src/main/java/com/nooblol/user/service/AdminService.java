package com.nooblol.user.service;

import com.nooblol.user.dto.AdminDeleteUserDto;
import com.nooblol.user.dto.AdminUpdateUserDto;
import com.nooblol.user.dto.AdminUserDto;
import com.nooblol.user.dto.UserSignOutDto;
import com.nooblol.user.dto.UserSignUpRequestDto;
import com.nooblol.global.dto.ResponseDto;

public interface AdminService {

  ResponseDto addAdminMember(UserSignUpRequestDto userSignUpRequestDto);

  ResponseDto deleteAdminMember(UserSignOutDto userSignOutDto);

  ResponseDto forceDeleteUser(AdminDeleteUserDto adminDeleteUserDto);

  ResponseDto getAllUserList(AdminUserDto adminUserDto);

  ResponseDto changeToActiveUser(AdminUpdateUserDto adminUpdateUserDto);

  ResponseDto changeToSuspensionUser(AdminUpdateUserDto adminUpdateUserDto);
}
