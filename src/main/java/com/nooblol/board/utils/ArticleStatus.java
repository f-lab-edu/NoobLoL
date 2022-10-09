package com.nooblol.board.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nooblol.global.exception.ExceptionMessage;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ArticleStatus {
  ACTIVE(1), SECRET(2);


  ArticleStatus(int status) {
    this.status = status;
  }

  int status;

  public static boolean isExistStatus(int statusType) {
    return Arrays.stream(ArticleStatus.values())
        .anyMatch(status -> status.getStatus() == statusType);
  }

  public static ArticleStatus findStatusEnumByString(String statusType) {
    return ArticleStatus.valueOf(statusType.toUpperCase());
  }


  public static ArticleStatus findStatusEnumByIntValue(int statusValue) {
    return Arrays.stream(ArticleStatus.values())
        .filter(articleStatusEnum -> articleStatusEnum.getStatus() == statusValue)
        .findFirst().get();
  }

  @JsonCreator
  public static ArticleStatus findByEnum(Object statusValue) {
    if (statusValue instanceof String) {
      return findStatusEnumByString(((String) statusValue).toUpperCase());
    }
    if (statusValue instanceof Integer) {
      return findStatusEnumByIntValue((Integer) statusValue);
    }

    throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
  }
}
