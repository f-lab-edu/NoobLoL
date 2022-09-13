package com.nooblol.global.utils;

import com.nooblol.community.dto.UserDto;
import javax.servlet.http.HttpSession;
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
}