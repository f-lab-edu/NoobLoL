package com.nooblol.user.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nooblol.user.dto.UserSignOutDto;
import com.nooblol.user.mapper.UserSignOutMapper;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.user.service.impl.UserSignOutServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class UserSignOutServiceImplTest {

  @Mock
  private UserSignOutMapper userSignOutMapper;

  @InjectMocks
  private UserSignOutServiceImpl userSignOutService;


  @Test
  @DisplayName("회원 탈퇴시 DB에 계정 데이터가 없는 경우, NO DATA를 가진 Exception을 획득한다.")
  void signOutUser_NoDataExceptionTest() {
    //given
    UserSignOutDto mockSignOutDto = new UserSignOutDto();
    mockSignOutDto.setPassword("abc");
    mockSignOutDto.setUserId("test");

    //mock
    when(userSignOutMapper.selectUserCount(mockSignOutDto)).thenReturn(0);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      userSignOutService.signOutUser(mockSignOutDto);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.NO_DATA);
  }

  @Test
  @DisplayName("회원 탈퇴시 DB에 계정 데이터가 존재하고 정상 삭제된 경우, 결과값으로 OK와 true값을 획득한다.")
  void signOutUser_ResponseOKTest() {
    //given
    UserSignOutDto mockSignOutDto = new UserSignOutDto();
    mockSignOutDto.setPassword("abc");
    mockSignOutDto.setUserId("test");

    //mock
    when(userSignOutMapper.selectUserCount(mockSignOutDto)).thenReturn(1);
    when(userSignOutMapper.deleteUser(mockSignOutDto)).thenReturn(1);

    //when
    ResponseDto result = userSignOutService.signOutUser(mockSignOutDto);

    //then
    assertEquals(result.getResultCode(), HttpStatus.OK.value());
    assertEquals(result.getResult(), true);
  }
}