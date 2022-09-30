package com.nooblol.board.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ArticleLikeStatusEnum {

  LIKE(true), NOT_LIKE(false);

  ArticleLikeStatusEnum(boolean likeStatus) {
    this.likeStatus = likeStatus;
  }

  boolean likeStatus;

  public static ArticleLikeStatusEnum findLikeStatusType(boolean likeStatus) {
    if (likeStatus) {
      return LIKE;
    }
    return NOT_LIKE;
  }


  public static ArticleLikeStatusEnum findLikeStatusByInt(int num) {
    if (num == 1) {
      return LIKE;
    }
    return NOT_LIKE;
  }
}
