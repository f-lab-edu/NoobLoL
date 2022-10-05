package com.nooblol.board.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nooblol.global.exception.ExceptionMessage;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ArticleStatusEnum {
  ACTIVE(1), SECRET(2);


  ArticleStatusEnum(int status) {
    this.status = status;
  }

  int status;

  public static boolean isExistStatus(int statusType) {
    return Arrays.stream(ArticleStatusEnum.values())
        .anyMatch(status -> status.getStatus() == statusType);
  }

  public static ArticleStatusEnum findStatusEnumByString(String statusType) {
    return ArticleStatusEnum.valueOf(statusType.toUpperCase());
  }


  public static ArticleStatusEnum findStatusEnumByIntValue(int statusValue) {
    return Arrays.stream(ArticleStatusEnum.values())
        .filter(articleStatusEnum -> articleStatusEnum.getStatus() == statusValue)
        .findFirst().get();
  }

  @JsonCreator
  public static ArticleStatusEnum findByEnum(Object statusValue) {
    if (statusValue instanceof String) {
      return findStatusEnumByString(((String) statusValue).toUpperCase());
    }
    if (statusValue instanceof Integer) {
      return findStatusEnumByIntValue((Integer) statusValue);
    }

    throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
  }
}
