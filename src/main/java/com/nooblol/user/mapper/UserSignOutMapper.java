package com.nooblol.user.mapper;

import com.nooblol.user.dto.UserSignOutDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserSignOutMapper {

  int selectUserCount(UserSignOutDto userSignOutDto);

  int deleteUser(UserSignOutDto userSignOutDto);
}
