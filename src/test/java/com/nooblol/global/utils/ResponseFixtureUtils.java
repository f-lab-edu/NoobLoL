package com.nooblol.global.utils;

import com.nooblol.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;

public class ResponseFixtureUtils {

  /**
   * ResponseDto의 result가 true이고 상태코드가 200인 Object
   *
   * @return
   */
  public static ResponseDto getResultCodeIsOkAndResultTrueFixture() {
    return new ResponseDto(HttpStatus.OK.value(), true);
  }
}
