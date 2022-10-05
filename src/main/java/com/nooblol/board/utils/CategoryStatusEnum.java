package com.nooblol.board.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nooblol.global.exception.ExceptionMessage;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum CategoryStatusEnum {
  ACTIVE(1), DEACTIVE(2), DELETE(9);


  CategoryStatusEnum(int status) {
    this.status = status;
  }

  int status;

  public static boolean isExistStatus(int statusType) {
    return Arrays.stream(CategoryStatusEnum.values())
        .anyMatch(status -> status.getStatus() == statusType);
  }

  public static CategoryStatusEnum findStatusEnumByString(String statusType) {
    return CategoryStatusEnum.valueOf(statusType.toUpperCase());
  }


  public static CategoryStatusEnum findStatusEnumByIntValue(int statusValue) {
    return Arrays.stream(CategoryStatusEnum.values())
        .filter(categoryStatusEnum -> categoryStatusEnum.getStatus() == statusValue)
        .findFirst().get();
  }

  @JsonCreator
  public static CategoryStatusEnum findByEnum(Object statusValue) {
    if (statusValue instanceof String) {
      return findStatusEnumByString(((String) statusValue).toUpperCase());
    }
    if (statusValue instanceof Integer) {
      return findStatusEnumByIntValue((Integer) statusValue);
    }

    throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
  }
}
