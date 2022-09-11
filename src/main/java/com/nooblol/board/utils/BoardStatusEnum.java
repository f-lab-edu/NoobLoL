package com.nooblol.board.utils;

import lombok.Getter;

@Getter
public enum BoardStatusEnum {
  ACTIVE(1), DEACTIVE(2), DELETE(9);


  BoardStatusEnum(int status) {
    this.status = status;
  }

  int status;


}
