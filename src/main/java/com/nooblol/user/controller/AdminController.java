package com.nooblol.user.controller;

import com.nooblol.user.dto.AdminDeleteUserDto;
import com.nooblol.user.dto.AdminUpdateUserDto;
import com.nooblol.user.dto.AdminUserDto;
import com.nooblol.user.dto.UserSignOutDto;
import com.nooblol.user.dto.UserSignUpRequestDto;
import com.nooblol.user.service.AdminService;
import com.nooblol.global.dto.ResponseDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
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

  @PostMapping("/userChangeActive")
  public ResponseDto changeToActiveUser(@Valid @RequestBody AdminUpdateUserDto adminUpdateUserDto) {
    return adminService.changeToActiveUser(adminUpdateUserDto);
  }

  @PostMapping("/userChangeToSuspension")
  public ResponseDto changeToSuspensionUser(
      @Valid @RequestBody AdminUpdateUserDto adminUpdateUserDto) {
    return adminService.changeToSuspensionUser(adminUpdateUserDto);
  }

}
