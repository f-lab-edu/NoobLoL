package com.nooblol.board.utils;

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

}
