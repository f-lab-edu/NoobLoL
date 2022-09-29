package com.nooblol.user.utils;

import com.nooblol.user.dto.UserDto;
import com.nooblol.user.dto.UserInfoUpdateDto;
import com.nooblol.user.dto.UserLoginDto;
import com.nooblol.user.dto.UserSignOutDto;
import com.nooblol.user.dto.UserSignUpRequestDto;

public class UserFixtureUtils {

  public static final String fixtureAuthUserId = "test";
  public static final String fixtureAuthUserEmail = "test@test.com";
  public static final String fixtureAuthUserName = "test";
  public static final String fixturePassword = "abc";


  /**
   * 생성할 유저 Object
   *
   * @return
   */
  public static UserSignUpRequestDto getCreateUserFixture() {
    return new UserSignUpRequestDto().builder()
        .userEmail(fixtureAuthUserEmail)
        .userName(fixtureAuthUserName)
        .password(fixturePassword)
        .build();
  }

  /**
   * 수정요청 DTO
   *
   * @return
   */
  public static UserInfoUpdateDto getUserInfoUpdateRequestFixture() {
    UserInfoUpdateDto returnVal = new UserInfoUpdateDto();
    returnVal.setUserId(fixtureAuthUserId);
    returnVal.setUserEmail(fixtureAuthUserEmail);
    returnVal.setOrgUserName(fixtureAuthUserName);
    returnVal.setOrgPassword(fixturePassword);
    returnVal.setUserRole(UserRoleStatus.AUTH_USER.getRoleValue());
    returnVal.setNewUserName("testAuthNewUserName");
    returnVal.setNewPassword("NewPassword");
    return returnVal;
  }

  public static UserSignOutDto getUserSignOutFixture() {
    UserSignOutDto returnVal = new UserSignOutDto();
    returnVal.setUserId(fixtureAuthUserId);
    returnVal.setPassword(fixturePassword);
    return returnVal;

  }

  public static UserLoginDto getUserLoginFixture() {
    UserLoginDto returnVal = new UserLoginDto();
    returnVal.setUserEmail(fixtureAuthUserEmail);
    returnVal.setUserPassword(fixturePassword);
    return returnVal;
  }

}
