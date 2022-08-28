package com.nooblol.community.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.community.dto.AdminDeleteUserDto;
import com.nooblol.community.dto.AdminUserDto;
import com.nooblol.community.dto.UserDto;
import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.mapper.AdminMapper;
import com.nooblol.community.mapper.UserSignUpMapper;
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
class AdminServiceImplTest {

  @InjectMocks
  private AdminServiceImpl adminService;

  @Mock
  private UserSignUpMapper userSignUpMapper;

  @Mock
  private AdminMapper adminMapper;

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

  @Test
  @DisplayName("사용자 강제삭제 시 파라미터로 받은 계정 정보가 DB에서 존재하지 않는 경우, FORBIDDEN Exception을 획득한다.")
  void forceDeleteUser_WhenDbDataNullThenForbiddenException() {
    //given
    AdminDeleteUserDto mockUserDto = new AdminDeleteUserDto();
    mockUserDto.setAdminUserId("adminTest1");
    mockUserDto.setAdminUserPassword("test1");
    mockUserDto.setDeleteUserId("testDeleteUserId");

    //mock
    when(adminMapper.selectAdminDto(mockUserDto)).thenReturn(null);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      adminService.forceDeleteUser(mockUserDto);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.FORBIDDEN);
  }

  @Test
  @DisplayName("사용자 강제삭제 시 파라미터로 받은 계정 정보가 관리자 계정이 아닌 경우, FORBIDDEN Exception을 획득한다.")
  void forceDeleteUser_WhenNotAdminThenForbiddenException() {
    //given
    AdminDeleteUserDto mockUserDto = new AdminDeleteUserDto();
    mockUserDto.setAdminUserId("adminTest1");
    mockUserDto.setAdminUserPassword("test1");
    mockUserDto.setDeleteUserId("testDeleteUserId");

    UserDto mockReturnUserDto = new UserDto();
    mockReturnUserDto.setUserId(mockUserDto.getAdminUserId());
    mockReturnUserDto.setUserRole(UserRoleStatus.AUTH_USER.getRoleValue());

    //mock
    when(adminMapper.selectAdminDto(mockUserDto)).thenReturn(mockReturnUserDto);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      adminService.forceDeleteUser(mockUserDto);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.FORBIDDEN);
  }

  @Test
  @DisplayName("사용자 강제삭제 시 파라미터로 받은 계정 정보가 관리자 계정이 맞고 삭제가 성공한경우, OK상태와 결과로 true를 획득한다.")
  void forceDeleteUser_WhenAdminThenReturnOkAndTrue() {
    //given
    AdminDeleteUserDto mockUserDto = new AdminDeleteUserDto();
    mockUserDto.setAdminUserId("adminTest1");
    mockUserDto.setAdminUserPassword("test1");
    mockUserDto.setDeleteUserId("testDeleteUserId");

    UserDto mockReturnUserDto = new UserDto();
    mockReturnUserDto.setUserId(mockUserDto.getAdminUserId());
    mockReturnUserDto.setUserRole(UserRoleStatus.ADMIN.getRoleValue());

    //mock
    when(adminMapper.selectAdminDto(mockUserDto)).thenReturn(mockReturnUserDto);
    when(adminMapper.forcedDeleteUser(mockUserDto.getDeleteUserId())).thenReturn(1);

    //when
    ResponseDto result = adminService.forceDeleteUser(mockUserDto);

    //then
    assertEquals(result.getResultCode(), HttpStatus.OK.value());
    assertEquals(result.getResult(), true);
  }

  @Test
  @DisplayName("사용자 강제삭제 시 파라미터로 받은 계정 정보가 관리자 계정이 맞고 삭제가 성공한경우, OK상태와 결과로 false를 획득한다.")
  void forceDeleteUser_WhenAdminThenReturnOkAndFalse() {
    //given
    AdminDeleteUserDto mockUserDto = new AdminDeleteUserDto();
    mockUserDto.setAdminUserId("adminTest1");
    mockUserDto.setAdminUserPassword("test1");
    mockUserDto.setDeleteUserId("testDeleteUserId");

    UserDto mockReturnUserDto = new UserDto();
    mockReturnUserDto.setUserId(mockUserDto.getAdminUserId());
    mockReturnUserDto.setUserRole(UserRoleStatus.ADMIN.getRoleValue());

    //mock
    when(adminMapper.selectAdminDto(mockUserDto)).thenReturn(mockReturnUserDto);
    when(adminMapper.forcedDeleteUser(mockUserDto.getDeleteUserId())).thenReturn(0);

    //when
    ResponseDto result = adminService.forceDeleteUser(mockUserDto);

    //then
    assertEquals(result.getResultCode(), HttpStatus.OK.value());
    assertEquals(result.getResult(), false);
  }

  @Test
  @DisplayName("모든 사용자 계정획득시, 파라미터로 받은 계정정보가 DB에 없는 경우 Forbidden Exception이 발생한다.")
  void getAllUserList_WhenAdminUserNoDataThenForbiddenException() {
    //given
    AdminUserDto mockUserDto = new AdminUserDto();
    mockUserDto.setAdminUserId("adminTest1");
    mockUserDto.setAdminUserPassword("testPassword1");

    //mock
    when(adminMapper.selectAdminDto(mockUserDto)).thenReturn(null);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      adminService.getAllUserList(mockUserDto);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.FORBIDDEN);
  }

  @Test
  @DisplayName("모든 사용자 계정획득시, 파라미터로 받은 계정이 관리자 권한이 아닌 경우 Forbidden Exception이 발생한다.")
  void getAllUserList_WhenNotAdminThenForbiddenException() {
    //given
    AdminUserDto mockUserDto = new AdminUserDto();
    mockUserDto.setAdminUserId("adminTest1");
    mockUserDto.setAdminUserPassword("testPassword1");

    UserDto mockReturnDto = new UserDto();
    mockReturnDto.setUserRole(UserRoleStatus.AUTH_USER.getRoleValue());

    //mock
    when(adminMapper.selectAdminDto(mockUserDto)).thenReturn(mockReturnDto);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      adminService.getAllUserList(mockUserDto);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.FORBIDDEN);
  }

  @Test
  @DisplayName("모든 사용자 계정획득시, 파라미터로 받은 계정이 관리자 계정인 경우 Response로 OK값을 획득한다.")
  void getAllUserList_WhenAdminThenResponseOk() {
    //given
    AdminUserDto mockUserDto = new AdminUserDto();
    mockUserDto.setAdminUserId("adminTest1");
    mockUserDto.setAdminUserPassword("testPassword1");

    UserDto mockReturnDto = new UserDto();
    mockReturnDto.setUserRole(UserRoleStatus.ADMIN.getRoleValue());

    //mock
    when(adminMapper.selectAdminDto(mockUserDto)).thenReturn(mockReturnDto);

    //when
    ResponseDto result = adminService.getAllUserList(mockUserDto);

    //then
    assertEquals(result.getResultCode(), HttpStatus.OK.value());
  }

}