package com.nooblol.user.utils;

import com.nooblol.global.utils.enumhandle.EnumConvertUtils;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

@Getter
public enum LetterStatus implements EnumConvertUtils {
  READ, UNREAD, DELETE;

  public static final LetterStatus[] SEARCH_STATUS_ARR = {
      READ, UNREAD
  };

  public static final LetterStatus[] STATUS_ALL_ARR = {
      READ, UNREAD, DELETE
  };


  public static boolean isExistStatus(String statusType) {
    try {
      LetterStatus.valueOf(statusType.toUpperCase());
    } catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }

  public static LetterType getLetterStatusByString(String statusType) {
    return LetterType.valueOf(statusType.toUpperCase());
  }


}
