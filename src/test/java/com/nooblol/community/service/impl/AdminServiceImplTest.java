package com.nooblol.community.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.mapper.UserSignUpMapper;
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
class AdminServiceImplTest {

  @InjectMocks
  private AdminServiceImpl adminService;

  @Mock
  private UserSignUpMapper userSignUpMapper;


  @Test
  @DisplayName("관리자를 계정을 추가할 때, DB에 삽입된 데이터가 없는경우, OK상태값과 false결과를 획득한다.")
  void addAdminMember_ResponseOkAndFalse() {
    //given
    UserSignUpRequestDto mockUserDto = new UserSignUpRequestDto();
    mockUserDto.setUserEmail("test@test.com");
    mockUserDto.setUserName("테스트 관리자1");
    mockUserDto.setPassword("sample1");

    //mock
    when(userSignUpMapper.insertSignUpUser(mockUserDto)).thenReturn(0);

    //when
    ResponseDto result = adminService.addAdminMember(mockUserDto);

    //then
    assertEquals(result.getResultCode(), HttpStatus.OK.value());
    assertEquals(result.getResult(), false);
  }

  @Test
  @DisplayName("관리자를 계정을 추가할 때, DB에 삽입된 데이터가 없는경우, OK상태값과 true 결과를 획득한다.")
  void addAdminMember_ResponseOkAndTrue() {
    //given
    UserSignUpRequestDto mockUserDto = new UserSignUpRequestDto();
    mockUserDto.setUserEmail("test@test.com");
    mockUserDto.setUserName("테스트 관리자1");
    mockUserDto.setPassword("sample1");

    //mock
    when(userSignUpMapper.insertSignUpUser(mockUserDto)).thenReturn(1);

    //when
    ResponseDto result = adminService.addAdminMember(mockUserDto);

    //then
    assertEquals(result.getResultCode(), HttpStatus.OK.value());
    assertEquals(result.getResult(), true);
  }

  @Test
  @DisplayName("관리자를 계정을 추가할 때, SQLException이 아닌 다른 Exception이 발생하는 경우, BAD REQUEST메세지를 포함한 Exception을 획득한다.")
  void addAdminMember_Exception() {
    //given
    UserSignUpRequestDto mockUserDto = new UserSignUpRequestDto();
    mockUserDto.setUserEmail("test@test.com");
    mockUserDto.setUserName("테스트 관리자1");
    mockUserDto.setPassword("sample1");

    //mock
    when(userSignUpMapper.insertSignUpUser(mockUserDto)).thenThrow(new RuntimeException());

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      adminService.addAdminMember(mockUserDto);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.BAD_REQUEST);
  }

}