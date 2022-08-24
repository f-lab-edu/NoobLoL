package com.nooblol.global.controller;

import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.ResponseEnum;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Validation 을 진행하게 될 경우 ResponseEntity가 반환이 아닌, ResponseDto가 응답이 되도록 하기 위한 설정
 */
@RestControllerAdvice
public class RestApiControllerAdvice {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @ExceptionHandler({ConstraintViolationException.class, IllegalArgumentException.class})
  public ResponseDto constraintViolationException(ConstraintViolationException e) {
    if (!ObjectUtils.isEmpty(e)) {
      e.getConstraintViolations().forEach(error -> {
        log.error("[" + error.getRootBeanClass() + "] :"
            + " ErrorTemplate : " + error.getMessageTemplate()
            + ", PropertyPath : " + error.getPropertyPath()
            + ", ErrorContent : " + error.getMessage()
        );
      });
    }
    return ResponseEnum.BAD_REQUEST.getResponse();
  }
}
