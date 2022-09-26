package com.nooblol.global.utils;

import com.nooblol.global.dto.ResponseDto;
import java.util.List;
import org.springframework.http.HttpStatus;

public class ResponseUtils {

  /**
   * List를 받아 공백여부를 확인한 이후 Return할 객체를 가공한다.
   *
   * @param list
   * @param <T>
   * @return
   */
  public static <T> ResponseDto makeListToResponseDto(List<T> list) {
    if (list == null || list.size() == 0) {
      return ResponseEnum.NOT_FOUND.getResponse();
    }
    return new ResponseDto(HttpStatus.OK.value(), list);
  }

}

