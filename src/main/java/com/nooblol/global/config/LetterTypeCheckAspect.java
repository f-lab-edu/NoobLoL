package com.nooblol.global.config;

import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionUtils;
import com.nooblol.user.utils.LetterType;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Order(3)
@Slf4j
@Component
public class LetterTypeCheckAspect {

  /**
   * 쪽지의 pathVariable에 대한 검사 사전에 User Login이 되어있는 경우에만 접속하기 떄문에 고의로 문제를 발생시키는 사용자 Id 같이 기록
   *
   * @param jp
   */
  @Before("@annotation(com.nooblol.global.annotation.LetterTypeValidation) && args(type,..)")
  public void letterTypeValidation(JoinPoint jp, String type) throws Throwable {
    if (!typeValid(type.toUpperCase())) {

      log.info("[LetterTypeCheckAspect.letterTypeValidation] Bad Request User Id : "
          + getSessionInUserId(jp));
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }
  }

  private boolean typeValid(String type) {
    return LetterType.isExistsType(type);
  }

  private String getSessionInUserId(JoinPoint jp) {
    HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest()
        .getSession();

    return SessionUtils.getSessionUserId(session);
  }
}
