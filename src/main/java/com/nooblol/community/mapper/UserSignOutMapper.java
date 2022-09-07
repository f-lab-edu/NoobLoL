package com.nooblol.community.mapper;

import com.nooblol.community.dto.UserSignOutDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserSignOutMapper {

  int selectUserCount(UserSignOutDto userSignOutDto);

  int deleteUser(UserSignOutDto userSignOutDto);
}
