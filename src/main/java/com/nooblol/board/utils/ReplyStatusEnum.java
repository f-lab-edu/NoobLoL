package com.nooblol.board.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nooblol.global.exception.ExceptionMessage;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ReplyStatusEnum {

  ACTIVE(1), DEACTIVE(2);


  ReplyStatusEnum(int status) {
    this.status = status;
  }

  int status;

  public static boolean isExistStatus(int statusType) {
    return Arrays.stream(ReplyStatusEnum.values())
        .anyMatch(status -> status.getStatus() == statusType);
  }

  public static ReplyStatusEnum findStatusEnumByStatusValue(int statusValue) {
    return Arrays.stream(ReplyStatusEnum.values())
        .filter(replyStatusEnum -> replyStatusEnum.getStatus() == statusValue)
        .findFirst().get();
  }

  @JsonCreator
  public static ReplyStatusEnum findByEnum(Object statusValue) {
    if (statusValue instanceof String) {
      return ReplyStatusEnum.valueOf(((String) statusValue).toUpperCase());
    }
    if (statusValue instanceof Integer) {
      return findStatusEnumByStatusValue((Integer) statusValue);
    }

    throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
  }
}
