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
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
      ConstraintViolationException e, HttpServletRequest request
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
      log.warn("Exception Trace : ", e);
    }
    return ResponseEnum.BAD_REQUEST.getResponse();
  }

  /*
  TODO : [22. 09. 08]
  지금 현재 전부 IllegalArgumentException으로 방향을 잡고있는데 그러면 이 Exception을 처리하는 메소드가
  혼자서 모든 부하를 담당하는게 되는데 처리에 있어서 분산을 하는게 맞지 않을까? 하는 고민이 든다.
   */
  @ExceptionHandler({IllegalArgumentException.class})
  public ResponseDto illegalArgumentException(
      IllegalArgumentException e, HttpServletRequest request
  ) {
    if (!ObjectUtils.isEmpty(e)) {
      log.warn(
          "[IllegalArgumentException] :"
              + "requestUrl: " + request.getRequestURI()
              + ", Error Message : " + e.getMessage()
      );
      log.warn("Exception Trace : ", e);
    }
    switch (e.getMessage()) {
      case ExceptionMessage.NO_DATA:
      case ExceptionMessage.NOT_FOUND:
        return ResponseEnum.NOT_FOUND.getResponse();

      case ExceptionMessage.SERVER_ERROR:
        return ResponseEnum.INTERNAL_SERVER_ERROR.getResponse();

      case ExceptionMessage.HAVE_DATA:
        ResponseDto rtn = ResponseEnum.CONFLICT.getResponse();
        rtn.setResult("이미 존재하는 데이터 입니다");
        return rtn;

      case ExceptionMessage.UNAUTHORIZED:
        return ResponseEnum.UNAUTHORIZED.getResponse();

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

  //PathVariable의 파라미터가 없는경우 해당 Exception이 실행 된다.
  @ExceptionHandler({NoHandlerFoundException.class})
  public ResponseDto noHandlerFoundExceptionHandling(NoHandlerFoundException e) {
    log.warn("[NoHandlerFoundExceptionHandling]", e);
    return ResponseEnum.BAD_REQUEST.getResponse();
  }

  /**
   * DELETE를 사용하며 PathVariable을 같이 사용하는 경우 PathVariable의 값이 없는채로 들어오는 경우 , 405 - MethodNowAllowed가
   * 발생함. 해당 Exception에 대한 Handler
   *
   * @param e
   * @return
   */
  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  public ResponseDto methodNotAllowedExceptionHandling(HttpRequestMethodNotSupportedException e) {
    log.warn("[HttpRequestMethodNotSupportedException]", e);
    return ResponseEnum.BAD_REQUEST.getResponse();
  }


  /**
   * Optinal을 사용하게 될 경우 발생하는 NPE에 대하여 Handling하기 위한 추가
   *
   * @param e
   * @return
   */
  @ExceptionHandler({NullPointerException.class})
  public ResponseDto nullPointerExceptionHandling(NullPointerException e) {
    log.warn("[NullPointerException]", e);
    return ResponseEnum.BAD_REQUEST.getResponse();
  }
}