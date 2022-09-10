package com.nooblol.global.utils;

import com.nooblol.user.utils.UserRoleStatus;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;

public class UserUtils {

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
    MessageDigest md = MessageDigest.getInstance("SHA-512");
    md.update(value.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(md.digest());
  }

  public static boolean isUserAdmin(int userRole) {
    return userRole == UserRoleStatus.ADMIN.getRoleValue();
  }

  public static boolean isNotUserAdmin(int userRole) {
    return userRole != UserRoleStatus.ADMIN.getRoleValue();
  }


  /**
   * 작성자와 Session에 저장된 UserId를 받아 해당 사용자가 동일한 UserId가 아닌 경우 True를 Return한다
   *
   * @param dbCreatedUserId DB에 저장된 CreatedUserId
   * @param sessionUserId   Session에 존재하는 로그인된 사용자 Id
   * @return
   */
  public static boolean isNotCreatedUser(String dbCreatedUserId, String sessionUserId) {
    return StringUtils.isBlank(dbCreatedUserId) || !dbCreatedUserId.equals(sessionUserId);
  }
}
