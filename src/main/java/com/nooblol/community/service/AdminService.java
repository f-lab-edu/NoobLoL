package com.nooblol.community.service;

import com.nooblol.community.dto.AdminDeleteUserDto;
import com.nooblol.community.dto.AdminUserDto;
import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.global.dto.ResponseDto;
import javax.servlet.http.HttpSession;

public interface AdminService {

  /**
   * 관리자 계정의 추가한다
   * @param userSignUpRequestDto
   * @param session
   * @return
   */
  ResponseDto addAdminMember(UserSignUpRequestDto userSignUpRequestDto, HttpSession session);

  /**
   * 관리자 계정을 삭제한다
   * @param userSignOutDto
   * @param session
   * @return
   */
  ResponseDto deleteAdminMember(UserSignOutDto userSignOutDto, HttpSession session);

  /**
   * 일반 사용자계정의 삭제한다
   * @param adminDeleteUserDto
   * @return
   */
  ResponseDto forceDeleteUser(AdminDeleteUserDto adminDeleteUserDto);

  /**
   * 모든 사용자 계정의 조회한다
   * @param adminUserDto
   * @return
   */
  ResponseDto getAllUserList(AdminUserDto adminUserDto);
}
