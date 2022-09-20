package com.nooblol.user.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.user.dto.UserDto;
import com.nooblol.user.dto.UserInfoUpdateDto;
import com.nooblol.user.dto.UserLoginDto;
import com.nooblol.user.mapper.UserInfoMapper;
import com.nooblol.user.service.impl.UserInfoServiceImpl;
import com.nooblol.user.utils.UserRoleStatus;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.util.ObjectUtils;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceImplTest {

  @InjectMocks
  private UserInfoServiceImpl userInfoService;

  @Mock
  private UserInfoMapper userInfoMapper;

  @Test
  @DisplayName("변경가능한 닉네임, 패스워드 두개가 모두 Null인 경우, BadRequest메세지를 가진 Exception을 획득한다.")
  void updateUserInfo_BadRequestExceptionTest() {
    //given
    UserInfoUpdateDto mockDto = new UserInfoUpdateDto();

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      userInfoService.updateUserInfo(mockDto);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.BAD_REQUEST);
  }

  @Test
  @DisplayName("DB에 수정된 데이터가 없는 경우, ResponseOK와 결과는 false를 획득한다.")
  void updateUserInfo_ResponseOkFalseTest() {
    //given
    UserInfoUpdateDto mockDto = new UserInfoUpdateDto();
    mockDto.setUserId("TestUserId");
    mockDto.setUserEmail("test@test.com");
    mockDto.setOrgUserName("기존 이름");
    mockDto.setNewUserName("새 이름");
    mockDto.setOrgPassword("sample");
    mockDto.setUserRole(UserRoleStatus.UNAUTH_USER.getRoleValue());

    //mock
    when(userInfoMapper.updateUserInfo(mockDto)).thenReturn(0);

    //when
    ResponseDto result = userInfoService.updateUserInfo(mockDto);

    //then
    assertEquals(result.getResultCode(), HttpStatus.OK.value());
    assertEquals(result.getResult(), false);
  }

  @Test
  @DisplayName("DB에 수정된 데이터가 존재하는 경우, ResponseOK와 결과는 True를 획득한다.")
  void updateUserInfo_ResponseOkTrueTest() {
    //given
    UserInfoUpdateDto mockDto = new UserInfoUpdateDto();
    mockDto.setUserId("TestUserId");
    mockDto.setUserEmail("test@test.com");
    mockDto.setOrgUserName("기존 이름");
    mockDto.setNewUserName("새 이름");
    mockDto.setOrgPassword("sample");
    mockDto.setUserRole(UserRoleStatus.UNAUTH_USER.getRoleValue());

    //mock
    when(userInfoMapper.updateUserInfo(mockDto)).thenReturn(1);

    //when
    ResponseDto result = userInfoService.updateUserInfo(mockDto);

    //then
    assertEquals(result.getResultCode(), HttpStatus.OK.value());
    assertEquals(result.getResult(), true);
  }

  @Test
  @DisplayName("로그인시 DB에 존재하지 않는 사용자 라면 BAD_REQUEST Exception이 발생한다.")
  void userLogin_WhenNotUserThenBadrequestException() {
    //given
    HttpSession session = new MockHttpSession();

    UserLoginDto mockUserLoginDto = new UserLoginDto();
    mockUserLoginDto.setUserEmail("test@Email.com");
    mockUserLoginDto.setUserPassword("testPassword");

    //mock
    when(userInfoMapper.selectUser(mockUserLoginDto)).thenReturn(null);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      userInfoService.userLogin(mockUserLoginDto, session);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.BAD_REQUEST);
  }

  @Test
  @DisplayName("로그인 시 해당 사용자가 활동정지인 경우, OK 상태값과 SUSPENSION_USER라는 사용자문구를 받는다.")
  void userLogin_WhenSuspensionUserThenOkAndGetStringSuspensionUser() {
    //given
    HttpSession session = new MockHttpSession();

    UserLoginDto mockUserLoginDto = new UserLoginDto();
    mockUserLoginDto.setUserEmail("test@Email.com");
    mockUserLoginDto.setUserPassword("testPassword");

    UserDto mockReturnDto = new UserDto();
    mockReturnDto.setUserRole(UserRoleStatus.SUSPENSION_USER.getRoleValue());

    //mock
    when(userInfoMapper.selectUser(mockUserLoginDto)).thenReturn(mockReturnDto);

    //when
    ResponseDto result = userInfoService.userLogin(mockUserLoginDto, session);

    //then
    assertEquals(result.getResultCode(), HttpStatus.OK.value());
    assertEquals(result.getResult(), UserRoleStatus.SUSPENSION_USER);
  }

  @Test
  @DisplayName("로그인 시 해당 사용자가 인증되지 않은 경우, OK 상태값과 UNAUTH_USER라는 사용자문구를 받는다.")
  void userLogin_WhenUnAuthUserThenOkAndGetStringSuspensionUser() {
    //given
    HttpSession session = new MockHttpSession();

    UserLoginDto mockUserLoginDto = new UserLoginDto();
    mockUserLoginDto.setUserEmail("test@Email.com");
    mockUserLoginDto.setUserPassword("testPassword");

    UserDto mockReturnDto = new UserDto();
    mockReturnDto.setUserRole(UserRoleStatus.UNAUTH_USER.getRoleValue());

    //mock
    when(userInfoMapper.selectUser(mockUserLoginDto)).thenReturn(mockReturnDto);

    //when
    ResponseDto result = userInfoService.userLogin(mockUserLoginDto, session);

    //then
    assertEquals(result.getResultCode(), HttpStatus.OK.value());
    assertEquals(result.getResult(), UserRoleStatus.UNAUTH_USER);
  }

  @Test
  @DisplayName("로그인 시 정상사용이 가능한 사용자인 경우, OK 상태값과 User정보를 획득한다.")
  void userLogin_WhenAuthUserThenOkAndUserInfo() {
    //given
    HttpSession session = new MockHttpSession();

    UserLoginDto mockUserLoginDto = new UserLoginDto();
    mockUserLoginDto.setUserEmail("test@Email.com");
    mockUserLoginDto.setUserPassword("testPassword");

    UserDto mockReturnDto = new UserDto();
    mockReturnDto.setUserId(UUID.randomUUID().toString());

    mockReturnDto.setUserRole(UserRoleStatus.AUTH_USER.getRoleValue());

    //mock
    when(userInfoMapper.selectUser(mockUserLoginDto)).thenReturn(mockReturnDto);

    //when
    ResponseDto result = userInfoService.userLogin(mockUserLoginDto, session);

    //then
    assertEquals(result.getResultCode(), HttpStatus.OK.value());
    assertEquals(!ObjectUtils.isEmpty((UserDto) result.getResult()), true);
  }

  @Test
  @DisplayName("로그아웃을 진행할 경우 무조건 OK 상태값을 획득한다.")
  void userLogout_WhenDoLogoutThenOk() {
    //given
    HttpSession session = new MockHttpSession();

    //when
    ResponseDto result = userInfoService.userLogout(session);

    //then
    assertEquals(result.getResultCode(), HttpStatus.OK.value());
  }

}