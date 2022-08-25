package com.nooblol.community.controller;

import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.service.UserSignUpService;
import com.nooblol.global.dto.ResponseDto;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserSignUpService userSignUpService;

  @PostMapping("/singup")
  public ResponseDto singUpSubmit(@Valid @RequestBody UserSignUpRequestDto userSignUpDto) {
    return userSignUpService.signUpUser(userSignUpDto);
  }

  /**
   * E-mail파라미터를 받아, 해당 메일주소를
   *
   * @param email
   * @return
   */
  @GetMapping("/resend-authmail/{email:.+}")
  public ResponseDto resendAuthMail(
      @PathVariable @NotBlank @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
          String email
  ) {
    return userSignUpService.reSendSignUpUserMail(email.trim());
  }

  /**
   * UserId를 받아 비활성화가 진행된 사용자의 UserRole을 활성상태인 `UserRoleStatus.AUTH_USER`로 변경함
   * @param userId users테이블의 userId Value
   * @return 404 : userId의 정상적인 입력이 아님, 500 : DB처리 도중 문제 발생, OK true: 정상적인 변경, OK false : Update
   * Fail
   */
  @GetMapping("/auth/{userId}")
  public ResponseDto authUserByMail(@PathVariable @NotBlank String userId) {
    return userSignUpService.changeRoleAuthUser(userId.trim());
  }

}
