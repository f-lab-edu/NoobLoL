package com.nooblol.user.utils;

import com.nooblol.global.utils.enumhandle.EnumConvertUtils;
import org.springframework.util.ObjectUtils;

public enum LetterType implements EnumConvertUtils {
  FROM, TO;

  public static boolean isExistsType(String type) {
    try {
      LetterType.valueOf(type.toUpperCase());
    } catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }

  public static LetterType getLetterTypeByString(String type) {
    return LetterType.valueOf(type.toUpperCase());
  }


}
