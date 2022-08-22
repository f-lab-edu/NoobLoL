package com.nooblol.community.service;

import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.global.dto.ResponseDto;
import java.util.Map;


/**
 * signUpUser : 회원가입
 * <p>
 * sendSignUpUserMail : 인증메일 발송
 * <p>
 * reSendSignUpUserMail : 인증메일 재발송
 * <p>
 * changeRoleAuthUser : 미인증 유저 권한 인증유저로 변경
 * <p>
 * getAuthMailTitle : 인증메일을 보내기 위한 파라미터 제작
 */
public interface UserSignUpService {

  ResponseDto signUpUser(UserSignUpRequestDto userDto);

  boolean sendSignUpUserMail(UserSignUpRequestDto userDto);

  ResponseDto reSendSignUpUserMail(String userEmail);

  ResponseDto changeRoleAuthUser(String userId);

  Map<String, String> getAuthMailTitle(UserSignUpRequestDto userDto);

}
