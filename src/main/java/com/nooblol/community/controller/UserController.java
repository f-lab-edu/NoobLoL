package com.nooblol.community.controller;

import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.service.UserSignUpService;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.ResponseEnum;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserSignUpService userSignUpService;

  @PostMapping("/singup")
  public ResponseDto singUpSubmit(@Validated UserSignUpRequestDto userSignUpDto, Errors erros) {
    if (erros.hasErrors()) {
      return new ResponseDto(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST);
    }

    return userSignUpService.signUpUser(userSignUpDto);
  }

  @GetMapping("/resend-authmail/{email:.+}")
  public ResponseDto resendAuthMail(@PathVariable @NotBlank String email) {
    if (StringUtils.isBlank(email)) {
      return ResponseEnum.NOT_FOUND.getResponse();
    }

    return userSignUpService.reSendSignUpUserMail(email.trim());
  }

  /**
   * @param userId users테이블의 userId Value
   * @return 404 : userId의 정상적인 입력이 아님, 500 : DB처리 도중 문제 발생, OK true: 정상적인 변경, OK false : Update
   * Fail
   */
  @GetMapping("/auth/{userId}")
  public ResponseDto authUserByMail(@PathVariable @NotBlank String userId) {
    if (StringUtils.isBlank(userId)) {
      return ResponseEnum.NOT_FOUND.getResponse();
    }

    return userSignUpService.changeRoleAuthUser(userId.trim());
  }

}
