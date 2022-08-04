package com.nooblol.global.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto<T> {

  private int resultCode;
  private T result;

  public ResponseDto(int resultCode, T result) {
    this.resultCode = resultCode;
    this.result = result;
  }
}