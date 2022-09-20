package com.nooblol.global.utils;

import lombok.Getter;

@Getter
public enum SessionEnum {
  USER_LOGIN("USER_LOGIN");

  String value;

  SessionEnum(String value) {
    this.value = value;
  }
}
