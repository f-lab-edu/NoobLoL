package com.nooblol.community.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.community.dto.UserInfoUpdateDto;
import com.nooblol.community.mapper.UserInfoMapper;
import com.nooblol.community.utils.UserRoleStatus;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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


}