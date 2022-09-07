package com.nooblol.global.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;

public class EncryptUtils {

  public static final String ENCRYPT_TYPE = "SHA-512";

  /**
   * 인수값 문자열을 SHA-512 로 암호화
   *
   * @param value 암호화를 진행할 문자열
   * @return 암호화가 진행된 문자열
   * @throws NoSuchAlgorithmException
   * @throws UnsupportedEncodingException
   */
  public static String stringChangeToSha512(String value)
      throws NoSuchAlgorithmException, UnsupportedEncodingException {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    MessageDigest md = MessageDigest.getInstance(ENCRYPT_TYPE);
    md.update(value.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(md.digest());
  }
}
