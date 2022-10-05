package com.nooblol.user.service;

import com.nooblol.user.dto.UserSignOutDto;
import com.nooblol.user.dto.UserSignUpRequestDto;
import com.nooblol.global.dto.ResponseDto;

/**
 * AdminService의 경우에는 Admin Controller에서 기능을 호출해서 사용을 하고 있으며, 해당 Controller의 AOP를 통해서 경우 사전에
 * Session에서 로그인한 사용자가 관리자인지 확인을 진행한다
 */
public interface AdminService {

  /**
   * 관리자 계정의 추가한다
   *
   * @param userSignUpRequestDto
   * @return
   */
  ResponseDto addAdminMember(UserSignUpRequestDto userSignUpRequestDto);

  /**
   * 관리자 계정을 삭제한다
   *
   * @param userSignOutDto
   * @return
   */
  ResponseDto deleteAdminMember(UserSignOutDto userSignOutDto);

  /**
   * 일반 사용자계정의 삭제한다
   *
   * @param deleteUserId
   * @return
   */
  ResponseDto forceDeleteUser(String deleteUserId);

  /**
   * 모든 사용자 계정의 조회한다 limitNum건 단위로 pageNum번에 맞춰서 조회한다.
   *
   * @param pageNum
   * @param limitNum
   * @return
   */
  ResponseDto getAllUserList(int pageNum, int limitNum);

  /**
   * 사용자의 Status를 활성상태(AUTH_USER)로 변경
   *
   * @param changeUserId
   * @return
   */
  ResponseDto changeToActiveUser(String changeUserId);

  /**
   * 사용자의 Status를 일시정지상태로 변경
   *
   * @param changeUserId
   * @return
   */
  ResponseDto changeToSuspensionUser(String changeUserId);

}
