package com.nooblol.global.utils;

import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

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


  /**
   * Object를 받아 ResponseDto를 반환한다.
   *
   * @param obj
   * @return
   */
  public static ResponseDto makeToResponseOkDto(Object obj) {
    if (ObjectUtils.isEmpty(obj)) {
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }
    ResponseDto result = ResponseEnum.OK.getResponse();
    result.setResult(obj);
    return result;
  }
}

