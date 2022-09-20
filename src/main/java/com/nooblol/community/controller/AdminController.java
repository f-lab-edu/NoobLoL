package com.nooblol.community.controller;

import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.service.AdminService;
import com.nooblol.community.utils.AdminConstants;
import com.nooblol.community.utils.UserConstants;
import com.nooblol.global.annotation.UserRoleIsAdminCehck;
import com.nooblol.global.dto.ResponseDto;
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
   * @return
   */
  @UserRoleIsAdminCehck
  @PostMapping("/signup")
  public ResponseDto signUpAdminMember(
      @Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto
  ) {
    return adminService.addAdminMember(userSignUpRequestDto);
  }


  /**
   * 관리자의 계정 탈퇴는 다른 관리자가 직접 진행해야 한다.
   *
   * @param userSignOutDto
   * @return
   */
  @UserRoleIsAdminCehck
  @PostMapping("/signout")
  public ResponseDto signOutAdminMember(@Valid @RequestBody UserSignOutDto userSignOutDto) {
    return adminService.deleteAdminMember(userSignOutDto);
  }

  /**
   * 관리자 권한으로, 사용자 계정의 강제 삭제
   *
   * @param deleteUserId
   * @return
   */
  @UserRoleIsAdminCehck
  @DeleteMapping("/forceUserDelete/{deleteUserId}")
  public ResponseDto deleteForcedUser(
      @PathVariable(required = false) @NotBlank(message = AdminConstants.ADMIN_DELETEUSERID_NULL) String deleteUserId
  ) {
    return adminService.forceDeleteUser(deleteUserId);
  }

  /**
   * 관리자 권한으로, 모든 사용자 계정의 조회
   *
   * @param pageNum  값이 제공되지 않으면 Default Value로 0이 들어가며 첫번째 페이지를 제공한다.
   * @param limitNum 값이 제공되지 않으면 Default Value로 30이 설정되며, 한번조회에 30개의 데이터가 제공된다.
   * @return
   */
  @UserRoleIsAdminCehck
  @GetMapping("/userList")
  public ResponseDto getAllUserList(
      @RequestParam(value = "page", defaultValue = "0") int pageNum,
      @RequestParam(value = "limit", defaultValue = "30") int limitNum
  ) {
    pageNum = pageNum * limitNum;
    return adminService.getAllUserList(pageNum, limitNum);
  }

  /**
   * 관리자 권한으로 특정 사용자의 권한을 활성화 상태로 변경한다.
   *
   * @param changeUserId 활성화 시키고자 하는 사용자의 UserId
   * @return
   */
  @UserRoleIsAdminCehck
  @PutMapping("/userChangeToActive/{changeUserId}")
  public ResponseDto changeToActiveUser(
      @PathVariable(required = false) @NotBlank(message = UserConstants.USER_ID_NULL) String changeUserId) {
    return adminService.changeToActiveUser(changeUserId);
  }

  /**
   * 관리자의 권한으로 특정 사용자의 권한을 일시정지 상태로 변경한다.
   *
   * @param changeUserId
   * @return
   */
  @UserRoleIsAdminCehck
  @PutMapping("/userChangeToSuspension/{changeUserId}")
  public ResponseDto changeToSuspensionUser(
      @PathVariable(required = false) @NotBlank(message = UserConstants.USER_ID_NULL) String changeUserId) {
    return adminService.changeToSuspensionUser(changeUserId);
  }
}
