package com.nooblol.board.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nooblol.global.exception.ExceptionMessage;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ReplyStatus {

  ACTIVE(1), DEACTIVE(2);


  ReplyStatus(int status) {
    this.status = status;
  }

  int status;

  public static boolean isExistStatus(int statusType) {
    return Arrays.stream(ReplyStatus.values())
        .anyMatch(status -> status.getStatus() == statusType);
  }

  public static ReplyStatus findStatusEnumByStatusValue(int statusValue) {
    return Arrays.stream(ReplyStatus.values())
        .filter(replyStatusEnum -> replyStatusEnum.getStatus() == statusValue)
        .findFirst().get();
  }

  @JsonCreator
  public static ReplyStatus findByEnum(Object statusValue) {
    if (statusValue instanceof String) {
      return ReplyStatus.valueOf(((String) statusValue).toUpperCase());
    }
    if (statusValue instanceof Integer) {
      return findStatusEnumByStatusValue((Integer) statusValue);
    }

    throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
  }
}
