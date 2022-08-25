package com.nooblol.global.controller;

import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.ResponseEnum;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Validation 을 진행하게 될 경우 ResponseEntity가 반환이 아닌, ResponseDto가 응답이 되도록 하기 위한 설정
 */
@RestControllerAdvice
public class RestApiControllerAdvice {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseDto constraintViolationException(
      ConstraintViolationException e,
      HttpServletRequest request
  ) {
    if (!ObjectUtils.isEmpty(e)) {
      e.getConstraintViolations().forEach(error -> {
        log.info(
            "[" + error.getRootBeanClass() + "] :"
                + "RequestUrl : " + request.getRequestURI()
                + ", ErrorTemplate : " + error.getMessageTemplate()
                + ", PropertyPath : " + error.getPropertyPath()
                + ", ErrorContent : " + error.getMessage()
        );
      });
    }
    return ResponseEnum.BAD_REQUEST.getResponse();
  }

  @ExceptionHandler({IllegalArgumentException.class})
  public ResponseDto illegalArgumentException(
      IllegalArgumentException e,
      HttpServletRequest request
  ) {
    if (!ObjectUtils.isEmpty(e)) {
      log.info(
          "[IllegalArgumentException] :"
              + "requestUrl: " + request.getRequestURI()
              + ", Error Message : " + e.getMessage()
      );
    }

    switch (e.getMessage()) {
      case ExceptionMessage.NO_DATA:
        return ResponseEnum.NOT_FOUND.getResponse();
      case ExceptionMessage.SERVER_ERROR:
        return ResponseEnum.INTERNAL_SERVER_ERROR.getResponse();
      default:
        return ResponseEnum.BAD_REQUEST.getResponse();
    }
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseDto methodValidException(
      MethodArgumentNotValidException e,
      HttpServletRequest request
  ) {
    if (!ObjectUtils.isEmpty(e.getBindingResult())) {
      BindingResult br = e.getBindingResult();
      if (br.hasErrors()) {
        FieldError er = br.getFieldError();
        log.info(
            "[MethodArgumentNotValidException] : "
                + " RequestUri : " + request.getRequestURI()
                + ", ErrorField : " + er.getField()
                + ", ErrorCodes : " + er.getCode()
                + ", Error Default Message : " + er.getDefaultMessage()
        );
      }
      log.info(
          "[MethodArgumentNotValidException] : "
              + ", ErrorStack : " + e.getStackTrace()
      );
    }

    return ResponseEnum.BAD_REQUEST.getResponse();
  }

  /*
  TODO : [22. 08. 25] NoHandlerFoundException 설정을 진행하여 Parameter가 없는 경우 해당 Exception으로 Return 할 수 있도록 계획함.
    <p>
    테스트는 두가지로 진행 해봄
    1. DispatcherServlet Bean을 가져와 dispatcherServlet.setThrowExceptionIfNoHandlerFound의 설정을 바꾸는 방법 → Fail
    2. application.yml에서 `spring.mvc.throw-exception-if-no-handler-found`의 설정과, `dispatch-options-request`을 진행하고서
     해당 메소드로 Handling하려 하였으나 안됨. 추후 다른 방법을 찾아봐야 한다.
   */
  @ExceptionHandler({NoHandlerFoundException.class})
  public ResponseDto noHandlerFoundExceptionHandling(NoHandlerFoundException e) {
    return ResponseEnum.BAD_REQUEST.getResponse();
  }
}