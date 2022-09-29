package com.nooblol.user.utils;

import com.nooblol.user.dto.UserDto;
import com.nooblol.user.dto.UserInfoUpdateDto;
import com.nooblol.user.dto.UserLoginDto;
import com.nooblol.user.dto.UserSignOutDto;
import com.nooblol.user.dto.UserSignUpRequestDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

  public static List<UserDto> getUserListFixture() {
    List<UserDto> fixtureUserList = new ArrayList<>();

    fixtureUserList.add(makeUserDto("test1", "test1@test.com", "test1"));
    fixtureUserList.add(makeUserDto("test2", "test2@test.com", "test2"));
    fixtureUserList.add(makeUserDto("test3", "test3@test.com", "test3"));
    fixtureUserList.add(makeUserDto("test4", "test4@test.com", "test4"));
    fixtureUserList.add(makeUserDto("test5", "test5@test.com", "test5"));
    fixtureUserList.add(makeUserDto("test6", "test6@test.com", "test6"));
    fixtureUserList.add(makeUserDto("test7", "test7@test.com", "test7"));
    fixtureUserList.add(makeUserDto("test8", "test8@test.com", "test8"));

    return fixtureUserList;
  }

  private static UserDto makeUserDto(String userId, String email, String userName) {
    return new UserDto().builder()
        .userId(userId)
        .userEmail(email)
        .userName(userName)
        .userRole(UserRoleStatus.AUTH_USER.getRoleValue())
        .level(1)
        .exp(0)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

}
