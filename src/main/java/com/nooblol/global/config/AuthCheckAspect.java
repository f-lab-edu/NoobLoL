package com.nooblol.global.config;

import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionUtils;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Slf4j
@Component
public class AuthCheckAspect {

  /**
   * 사용자로그인이 필요한 기능에서 AOP를 활용하여 사전에 로그인이 안된 사용자를 거름
   *
   * @param jp
   */
  @Before("@annotation(com.nooblol.global.annotation.UserLoginCheck)")
  public void memberLoginCheck(JoinPoint jp) throws Throwable {
    log.debug("AOP - memberLoginCheck Running");

    HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest()
        .getSession();

    String userId = SessionUtils.getSessionUserId(session);
    if (StringUtils.isBlank(userId)) {
      throw new IllegalArgumentException(ExceptionMessage.UNAUTHORIZED);
    }
  }
}
