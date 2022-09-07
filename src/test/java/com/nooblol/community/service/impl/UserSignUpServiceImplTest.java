package com.nooblol.community.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.mapper.UserSignUpMapper;
import com.nooblol.community.service.UserSendMailService;
import com.nooblol.community.utils.UserRoleStatus;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class UserSignUpServiceImplTest {

  @InjectMocks
  private UserSignUpServiceImpl userSignUpService;

  @Mock
  private UserSignUpMapper userSignUpMapper;

  @Mock
  private UserSendMailService userSendMailService;
  @Mock
  private Environment environment;

  @Test
  @DisplayName("회원가입 진행시 DB에 존재하지 않는 사용자인 경우에는 OK상태값을 획득한다.")
  void signUpUserTest() {
    //given
    UserSignUpRequestDto mockUserDto = new UserSignUpRequestDto();
    mockUserDto.setUserName("테스트1");
    mockUserDto.setUserEmail("test@naver.com");
    mockUserDto.setPassword("abcde");

    //mock
    when(userSignUpMapper.insertSignUpUser(mockUserDto)).thenReturn(1);
    when(userSignUpService.sendSignUpUserMail(mockUserDto)).thenReturn(true);

    //when
    ResponseDto result = userSignUpService.signUpUser(mockUserDto);

    assertEquals(result.getResultCode(), HttpStatus.OK.value());
  }

  @Test
  @DisplayName("회원가입 진행시 DB에 존재하는 사용자인 경우 HAVE_DATA라는 메세지를 가진 Exception을 획득한다.")
  void signUpUserUniqueExceptionTest() {
    //given
    UserSignUpRequestDto mockUserDto = new UserSignUpRequestDto();
    mockUserDto.setUserName("테스트1");
    mockUserDto.setUserEmail("test@naver.com");
    mockUserDto.setPassword("abcde");
    JdbcSQLIntegrityConstraintViolationException se =
        new JdbcSQLIntegrityConstraintViolationException(null, null, null, 23505, null, null);

    DuplicateKeyException mockException = new DuplicateKeyException("error", se);

    //mock
    when(userSignUpMapper.insertSignUpUser(mockUserDto))
        .thenThrow(mockException);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      userSignUpService.signUpUser(mockUserDto);
    });

    assertEquals(e.getMessage(), ExceptionMessage.HAVE_DATA);
  }

  @Test
  @DisplayName("인증메일 재발송시, DB에 없는 사용자인 경우 NO_DATA라는 메세지를 가진 Exception을 획득한다")
  void reSendSignUpUserMail_NoDataExceptionTest() {
    //given
    String testEmail = "test";

    //mock
    when(userSignUpMapper.selectUserInfoByEmail(testEmail)).thenReturn(null);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      userSignUpService.reSendSignUpUserMail(testEmail);
    });

    assertEquals(e.getMessage(), ExceptionMessage.NO_DATA);
  }

  @Test
  @DisplayName("인증메일을 다시 발송시, 계정의 Role이 활성화가 된 상태라면 Exception을 획득한다")
  void reSendSignUpUserMail_RoleNotEnoughExceptionTest() {
    //given
    String testEmail = "test@test.com";

    UserSignUpRequestDto mockUserDto = new UserSignUpRequestDto();
    mockUserDto.setUserName("테스트");
    mockUserDto.setUserEmail(testEmail);
    mockUserDto.setUserRole(UserRoleStatus.AUTH_USER.getRoleValue());

    //mock
    when(userSignUpMapper.selectUserInfoByEmail(testEmail)).thenReturn(mockUserDto);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      userSignUpService.reSendSignUpUserMail(testEmail);
    });

    assertEquals(e.getMessage(), mockUserDto.getUserName() + "님의 계정은 활성화가 필요한 상태가 아닙니다.");
  }

  @Test
  @DisplayName("인증메일을 다시 발송시, 계정의 Role이 비활성화 상태라면 OK를 획득한다.")
  void reSendSignUpUserMail_ResponseOk() {
    //given
    String testEmail = "test@test.com";

    UserSignUpRequestDto mockUserDto = new UserSignUpRequestDto();
    mockUserDto.setUserName("테스트");
    mockUserDto.setUserEmail(testEmail);

    //mock
    when(userSignUpMapper.selectUserInfoByEmail(testEmail)).thenReturn(mockUserDto);

    //when
    ResponseDto response = userSignUpService.reSendSignUpUserMail(testEmail);

    assertEquals(response.getResultCode(), HttpStatus.OK.value());
  }

  @Test
  @DisplayName("유저 권한 변경시 DB에 데이터가 없는 사용자인 경우 NO_DATA라는 메세지를 가진 Exception을 획득한다.")
  void chageRoleAuthUser_NoDataExceptionTest() {
    //given
    String nullUserId = "abcde";

    //mock
    when(userSignUpMapper.selectUserInfoByUserId(nullUserId)).thenReturn(null);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      userSignUpService.changeRoleAuthUser(nullUserId);
    });

    assertEquals(e.getMessage(), ExceptionMessage.NO_DATA);
  }

  @Test
  @DisplayName("유저 권한 변경시 이미 계정이 활성화 되어있는 사용자인 경우 Exception을 획득한다.")
  void chageRoleAuthUser_RoleNotEnoughExceptionTest() {
    //given
    String userId = "abcde";

    UserSignUpRequestDto mockUserDto = new UserSignUpRequestDto();
    mockUserDto.setUserId(userId);
    mockUserDto.setUserName("테스트");
    mockUserDto.setUserEmail("test@test.com");
    mockUserDto.setUserRole(UserRoleStatus.AUTH_USER.getRoleValue());

    //mock
    when(userSignUpMapper.selectUserInfoByUserId(userId)).thenReturn(mockUserDto);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      userSignUpService.changeRoleAuthUser(userId);
    });

    assertEquals(e.getMessage(), mockUserDto.getUserName() + "님의 계정은 활성화가 필요한 상태가 아닙니다.");
  }

  @Test
  @DisplayName("유저 권한 변경시 계정이 비활성화 되어있는 사용자인 경우 ResponseOk를 획득한다.")
  void chageRoleAuthUser_ResponseOk() {
    //given
    String userId = "abcde";

    UserSignUpRequestDto mockUserDto = new UserSignUpRequestDto();
    mockUserDto.setUserId(userId);
    mockUserDto.setUserName("테스트");
    mockUserDto.setUserEmail("test@test.com");

    //mock
    when(userSignUpMapper.selectUserInfoByUserId(userId)).thenReturn(mockUserDto);

    //when
    ResponseDto result = userSignUpService.changeRoleAuthUser(userId);

    assertEquals(result.getResultCode(), HttpStatus.OK.value());
  }

}