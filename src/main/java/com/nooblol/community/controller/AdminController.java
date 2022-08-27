package com.nooblol.community.controller;

import com.nooblol.community.dto.AdminDeleteUserDto;
import com.nooblol.community.dto.AdminUserDto;
import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.service.AdminService;
import com.nooblol.global.dto.ResponseDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final AdminService adminService;

  @PostMapping("/signup")
  public ResponseDto signUpAdminMember(
      @Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto
  ) {
    return adminService.addAdminMember(userSignUpRequestDto);
  }

  @PostMapping("/signout")
  public ResponseDto signOutAdminMember(@Valid @RequestBody UserSignOutDto userSignOutDto) {
    return adminService.deleteAdminMember(userSignOutDto);
  }

  @PostMapping("/forceDeleteUser")
  public ResponseDto adminForcedDeleteuser(
      @Valid @RequestBody AdminDeleteUserDto adminDeleteUserDto) {
    return adminService.forceDeleteUser(adminDeleteUserDto);
  }

  @PostMapping("/userList")
  public ResponseDto getAllUserList(@Valid @RequestBody AdminUserDto adminUserDto) {
    return adminService.getAllUserList(adminUserDto);
  }
}
