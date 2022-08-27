package com.nooblol.community.mapper;

import com.nooblol.community.dto.AdminUserDto;
import com.nooblol.community.dto.UserDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {

  UserDto selectAdminDto(AdminUserDto adminDto);

  List<UserDto> getAllUserList();

  int forcedDeleteUser(String deleteUserId);
}
