package com.nooblol.board.utils;

import lombok.Getter;

@Getter
public enum BoardStatusEnum {
  ACTIVE(1), DEACTIVE(2);

  BoardStatusEnum(int status) {
    this.status = status;
  }

  int status;


}
