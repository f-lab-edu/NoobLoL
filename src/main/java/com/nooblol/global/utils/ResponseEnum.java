package com.nooblol.global.utils;

import com.nooblol.global.dto.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseEnum {
  BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST),

  NOT_FOUND(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND),

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR),

  UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED),

  OK(HttpStatus.OK.value(), null),

  CONFLICT(HttpStatus.CONFLICT.value(), null);

  ResponseDto response;

  <T> ResponseEnum(int resultCode, T result) {
    this.response = new ResponseDto(resultCode, result);
  }

}