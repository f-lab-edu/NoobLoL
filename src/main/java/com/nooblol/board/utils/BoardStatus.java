package com.nooblol.board.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nooblol.global.exception.ExceptionMessage;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum BoardStatus {
  ACTIVE(1), DEACTIVE(2), DELETE(9);


  BoardStatus(int status) {
    this.status = status;
  }

  int status;

  public static boolean isExistStatus(int statusType) {
    return Arrays.stream(BoardStatus.values())
        .anyMatch(status -> status.getStatus() == statusType);
  }

  public static BoardStatus findStatusEnumByString(String statusType) {
    return BoardStatus.valueOf(statusType.toUpperCase());
  }


  public static BoardStatus findStatusEnumByIntValue(int statusValue) {
    return Arrays.stream(BoardStatus.values())
        .filter(boardStatusEnum -> boardStatusEnum.getStatus() == statusValue)
        .findFirst().get();
  }

  @JsonCreator
  public static BoardStatus findByEnum(Object statusValue) {
    if (statusValue instanceof String) {
      return findStatusEnumByString(((String) statusValue).toUpperCase());
    }
    if (statusValue instanceof Integer) {
      return findStatusEnumByIntValue((Integer) statusValue);
    }

    throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
  }
}
