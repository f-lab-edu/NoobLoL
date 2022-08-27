package com.nooblol.community.controller;

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
}
