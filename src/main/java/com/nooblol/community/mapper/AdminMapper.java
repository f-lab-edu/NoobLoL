package com.nooblol.community.mapper;

import com.nooblol.community.dto.AdminUserDto;
import com.nooblol.community.dto.UserDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {

  UserDto selectAdminDto(AdminUserDto adminDto);

  List<UserDto> getAllUserList(@Param("pageNum") int pageNum, @Param("limitNum") int limitNum);

  int forcedDeleteUser(String deleteUserId);
}
