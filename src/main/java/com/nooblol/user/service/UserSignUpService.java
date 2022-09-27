package com.nooblol.user.service;

import com.nooblol.user.dto.UserSignUpRequestDto;
import com.nooblol.global.dto.ResponseDto;
import java.util.Map;

public interface UserSignUpService {

  /**
   * 사용자의 회원가입
   *
   * @param userDto
   * @return
   */
  ResponseDto signUpUser(UserSignUpRequestDto userDto);

  /**
   * 사용자가 회원가입을 하게 될 경우 인증메일을 발송한다
   *
   * @param userDto
   * @return
   */
  boolean sendSignUpUserMail(UserSignUpRequestDto userDto);

  /**
   * 사용자가 회원가입을 할 때 인증메일의 재발송요청시, 다시 발송한다.
   *
   * @param userEmail Email을 파라미터로 받으며 DB에 존재하는 경우에만 재발송한다.
   * @return
   */
  ResponseDto reSendSignUpUserMail(String userEmail);

  /**
   * 이메일이 인증되지 않은 사용자의 경우 Role을 계정사용이 가능하도록 변경한다.
   *
   * @param userId
   * @return
   */
  ResponseDto changeRoleAuthUser(String userId);

  /**
   * 인증메일을 보내기 위한 파라미터(메일 제목, 내용)을 Map으로 제작 한다.
   *
   * @param userDto
   * @return
   */
  Map<String, String> getAuthMailContent(UserSignUpRequestDto userDto);

}
