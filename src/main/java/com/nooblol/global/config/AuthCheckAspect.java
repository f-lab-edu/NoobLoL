package com.nooblol.global.config;

import com.nooblol.user.utils.UserRoleStatus;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionUtils;
import java.util.Optional;
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
@Slf4j
@Order(1)
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

    if (Optional.ofNullable(SessionUtils.getSessionUserId(session)).isEmpty()) {
      throw new IllegalArgumentException(ExceptionMessage.UNAUTHORIZED);
    }
  }


  /**
   * 사용자 로그인여부, 사용자 관리자인지 동시에 확인한다.
   *
   * @param jp
   */
  @Before("@annotation(com.nooblol.global.annotation.UserRoleIsAdminCehck)")
  public void memberLoginAndRoleAdminCheck(JoinPoint jp) throws Throwable {
    log.debug("AOP - memberLoginAdminCheck");

    HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest()
        .getSession();

    //사용자 정보가 존재하지 않는 경우 GUEST로 설정
    Integer userRole = Optional
        .ofNullable(SessionUtils.getSessionUserRole(session))
        .orElse(UserRoleStatus.GUEST.getRoleValue());

    if (userRole.equals(UserRoleStatus.ADMIN.getRoleValue())) {
      throw new IllegalArgumentException(ExceptionMessage.UNAUTHORIZED);
    }
  }
}
