package com.nooblol.global.utils;

import javax.servlet.http.HttpSession;
import org.springframework.mock.web.MockHttpSession;

public class SessionSampleObject {

  public static final HttpSession authUserLoginSession = getAuthUserLoginMockSession();
  public static final HttpSession adminUserLoginSession = getAdminUserLoginMockSession();

  private static HttpSession getAuthUserLoginMockSession() {
    HttpSession session = new MockHttpSession();
    session.setAttribute(SessionEnum.USER_LOGIN.getValue(),
        UserSampleObject.authUserDto
    );

    return session;
  }

  private static HttpSession getAdminUserLoginMockSession() {
    HttpSession session = new MockHttpSession();
    session.setAttribute(SessionEnum.USER_LOGIN.getValue(),
        UserSampleObject.adminUserDto
    );

    return session;
  }


}
