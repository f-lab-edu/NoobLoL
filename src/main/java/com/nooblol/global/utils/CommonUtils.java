package com.nooblol.global.utils;

public class CommonUtils {

  public static String summonerNameWhiteSpaceReplace(String name) {
    if (name == null) {
      return null;
    }
    return name.replaceAll(" ", "");
  }

  public static Boolean objectIsNull(Object obj) {
    return obj == null;
  }

  public static Boolean objectIsNotNull(Object obj) {
    return obj != null;
  }

}
