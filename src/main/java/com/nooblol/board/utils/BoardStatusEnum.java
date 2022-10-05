package com.nooblol.board.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nooblol.global.exception.ExceptionMessage;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum BoardStatusEnum {
  ACTIVE(1), DEACTIVE(2), DELETE(9);


  BoardStatusEnum(int status) {
    this.status = status;
  }

  int status;

  public static boolean isExistStatus(int statusType) {
    return Arrays.stream(BoardStatusEnum.values())
        .anyMatch(status -> status.getStatus() == statusType);
  }

  public static BoardStatusEnum findStatusEnumByString(String statusType) {
    return BoardStatusEnum.valueOf(statusType.toUpperCase());
  }


  public static BoardStatusEnum findStatusEnumByIntValue(int statusValue) {
    return Arrays.stream(BoardStatusEnum.values())
        .filter(boardStatusEnum -> boardStatusEnum.getStatus() == statusValue)
        .findFirst().get();
  }

  @JsonCreator
  public static BoardStatusEnum findByEnum(Object statusValue) {
    if (statusValue instanceof String) {
      return findStatusEnumByString(((String) statusValue).toUpperCase());
    }
    if (statusValue instanceof Integer) {
      return findStatusEnumByIntValue((Integer) statusValue);
    }

    throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
  }
}
