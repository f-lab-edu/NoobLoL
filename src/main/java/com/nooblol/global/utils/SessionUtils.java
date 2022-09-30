package com.nooblol.global.utils;

import com.nooblol.user.dto.UserDto;
import com.nooblol.user.utils.UserRoleStatus;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

public class SessionUtils {


  private SessionUtils() {
  }

  public static String getSessionUserId(HttpSession session) {
    UserDto userAttribute = (UserDto) session.getAttribute(SessionEnum.USER_LOGIN.getValue());
    if (ObjectUtils.isEmpty(userAttribute)) {
      return null;
    }
    return userAttribute.getUserId();
  }

  public static Integer getSessionUserRole(HttpSession session) {
    UserDto userAttribute = (UserDto) session.getAttribute(SessionEnum.USER_LOGIN.getValue());
    if (ObjectUtils.isEmpty(userAttribute)) {
      return null;
    }
    return userAttribute.getUserRole();
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
