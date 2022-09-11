package com.nooblol.community.controller;

import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.service.AdminService;
import com.nooblol.community.utils.AdminConstants;
import com.nooblol.global.dto.ResponseDto;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//TODO : AOP로 UserLoginCheck, UserRoleAdminCheck 2개에 대해서 적용필요

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
   *
   * @param deleteUserId
   * @param session
   * @return
   */
  @DeleteMapping("/forceUserDelete/{deleteUserId}")
  public ResponseDto deleteForcedUser(
      @PathVariable(required = false) @NotBlank(message = AdminConstants.ADMIN_DELETEUSERID_NULL) String deleteUserId,
      HttpSession session) {
    return adminService.forceDeleteUser(deleteUserId, session);
  }

  /**
   * 관리자 권한으로, 모든 사용자 계정의 조회
   *
   * @param pageNum  값이 제공되지 않으면 Default Value로 0이 들어가며 첫번째 페이지를 제공한다.
   * @param limitNum 값이 제공되지 않으면 Default Value로 30이 설정되며, 한번조회에 30개의 데이터가 제공된다.
   * @param session
   * @return
   */
  @GetMapping("/userList")
  public ResponseDto getAllUserList(
      @RequestParam(value = "page", defaultValue = "0") int pageNum,
      @RequestParam(value = "limit", defaultValue = "30") int limitNum,
      HttpSession session) {
    pageNum = pageNum * 30;
    return adminService.getAllUserList(pageNum, limitNum, session);
  }


  @PutMapping("/userChangeToActive/{changeUserId}")
  public ResponseDto changeToActiveUser(
      @PathVariable(required = false) @NotBlank(message = AdminConstants.ADMIN_USERID_NULL) String changeUserId,
      HttpSession session) {
    return adminService.changeToActiveUser(changeUserId, session);
  }

  @PutMapping("/userChangeToSuspension/{changeUserId}")
  public ResponseDto changeToSuspensionUser(
      @PathVariable(required = false) @NotBlank(message = AdminConstants.ADMIN_USERID_NULL) String changeUserId,
      HttpSession session) {
    return adminService.changeToSuspensionUser(changeUserId, session);
  }
}
