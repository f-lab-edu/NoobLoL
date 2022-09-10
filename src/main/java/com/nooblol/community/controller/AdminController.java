package com.nooblol.community.controller;

import com.nooblol.community.dto.AdminDeleteUserDto;
import com.nooblol.community.dto.AdminUserDto;
import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.service.AdminService;
import com.nooblol.global.dto.ResponseDto;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {


  private final AdminService adminService;


  /**
   * 관리자 계정의 생성은, 관리자가 직접 생성해야 한다.
   *
   * @param userSignUpRequestDto
   * @param session
   * @return
   */
  @PostMapping("/signup")
  public ResponseDto signUpAdminMember(
      @Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto, HttpSession session
  ) {
    return adminService.addAdminMember(userSignUpRequestDto, session);
  }


  /**
   * 관리자의 계정 탈퇴는 다른 관리자가 직접 진행해야 한다.
   *
   * @param userSignOutDto
   * @return
   */
  @PostMapping("/signout")
  public ResponseDto signOutAdminMember(
      @Valid @RequestBody UserSignOutDto userSignOutDto,
      HttpSession session
  ) {
    return adminService.deleteAdminMember(userSignOutDto, session);
  }

  /**
   * 관리자 권한으로, 사용자 계정의 강제 삭제
   * @param adminDeleteUserDto
   * @param session
   * @return
   */
  @PostMapping("/forceDeleteUser")
  public ResponseDto adminForcedDeleteuser(
      @Valid @RequestBody AdminDeleteUserDto adminDeleteUserDto, HttpSession session) {
    return adminService.forceDeleteUser(adminDeleteUserDto);
  }

  /**
   * 관리자 권한으로, 모든 사용자 계정의 조회
   * @param adminUserDto
   * @param session
   * @return
   */
  @PostMapping("/userList")
  public ResponseDto getAllUserList(@Valid @RequestBody AdminUserDto adminUserDto,
      HttpSession session) {
    return adminService.getAllUserList(adminUserDto);
  }
}
