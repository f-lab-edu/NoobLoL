package com.nooblol.global.utils;

import com.nooblol.global.dto.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ResponseDto를 다들 공통된 리턴으로 하고자 제작. OK의 경우엔 setResponseResult로 추가적으로 Object를 삽입해줘야 한다.
 */
@Getter
public enum ResponseEnum {
  BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다. 파라미터를 확인 해주세요."),
  NOT_FOUND(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 에러"),
  OK(HttpStatus.OK.value(), null);

  ResponseDto response;

  <T> ResponseEnum(int resultCode, T result) {
    this.response = new ResponseDto(resultCode, result);
  }

  <T> void setResponseResult(T result) {
    this.response.setResult(result);
  }

}