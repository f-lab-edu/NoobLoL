package com.nooblol.board.utils;

import lombok.Getter;

@Getter
public enum ArticleStatusEnum {
  ACTIVE(1), SECRET(2);


  ArticleStatusEnum(int status) {
    this.status = status;
  }

  int status;


}
