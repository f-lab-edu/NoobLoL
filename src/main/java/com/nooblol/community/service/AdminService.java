package com.nooblol.community.service;

import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.global.dto.ResponseDto;
import javax.servlet.http.HttpSession;

public interface AdminService {

  /**
   * 관리자 계정의 추가한다
   *
   * @param userSignUpRequestDto
   * @param session
   * @return
   */
  ResponseDto addAdminMember(UserSignUpRequestDto userSignUpRequestDto, HttpSession session);

  /**
   * 관리자 계정을 삭제한다
   *
   * @param userSignOutDto
   * @param session
   * @return
   */
  ResponseDto deleteAdminMember(UserSignOutDto userSignOutDto, HttpSession session);

  /**
   * 일반 사용자계정의 삭제한다
   *
   * @param deleteUserId
   * @param session
   * @return
   */
  ResponseDto forceDeleteUser(String deleteUserId, HttpSession session);

  /**
   * 모든 사용자 계정의 조회한다 limitNum건 단위로 pageNum번에 맞춰서 조회한다.
   *
   * @param pageNum
   * @param limitNum
   * @param session
   * @return
   */
  ResponseDto getAllUserList(int pageNum, int limitNum, HttpSession session);
}
