package com.nooblol.global.utils;

public class StringUtils {

  public static String summonerNameWhiteSpaceReplace(String name) {
    if (name == null) {
      return null;
    }
    return name.replaceAll(" ", "");
  }
}
