package com.nooblol.board.utils;

import lombok.Getter;

@Getter
public enum ArticleLikeStatus {

  LIKE(true), NOT_LIKE(false);

  ArticleLikeStatus(boolean likeStatus) {
    this.likeStatus = likeStatus;
  }

  boolean likeStatus;

  public static ArticleLikeStatus findLikeStatusType(boolean likeStatus) {
    if (likeStatus) {
      return LIKE;
    }
    return NOT_LIKE;
  }


  public static ArticleLikeStatus findLikeStatusByInt(int num) {
    if (num == 1) {
      return LIKE;
    }
    return NOT_LIKE;
  }
}
