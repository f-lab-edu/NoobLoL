package com.nooblol.user.mapper;

import com.nooblol.user.dto.AdminUserDto;
import com.nooblol.user.dto.UserDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {

  UserDto selectAdminDto(AdminUserDto adminDto);

  List<UserDto> getAllUserList();

  int forcedDeleteUser(String deleteUserId);

  int changeUserRole(UserDto userDto);
}
