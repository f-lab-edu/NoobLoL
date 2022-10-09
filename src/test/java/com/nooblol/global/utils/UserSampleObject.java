package com.nooblol.global.utils;

import com.nooblol.user.dto.UserDto;
import com.nooblol.user.utils.UserRoleStatus;

public class UserSampleObject {

  public static final UserDto authUserDto = makeTestAuthUserDto();
  public static final UserDto adminUserDto = makeTestAdminUserDto();

  /**
   * 테스트를 위한 일반 사용자정보
   *
   * @return
   */
  private static UserDto makeTestAuthUserDto() {
    return new UserDto().builder()
        .userId("test")
        .userEmail("test@test.com")
        .userName("test")
        .userRole(UserRoleStatus.AUTH_USER.getRoleValue())
        .level(1)
        .exp(0)
        .build();
  }

  /**
   * 테스트를 위한 관리자 사용자정보
   *
   * @return
   */
  private static UserDto makeTestAdminUserDto() {
    return new UserDto().builder()
        .userId("admin")
        .userEmail("admin@test.com")
        .userName("admin")
        .userRole(UserRoleStatus.ADMIN.getRoleValue())
        .level(1)
        .exp(0)
        .build();
  }

}
