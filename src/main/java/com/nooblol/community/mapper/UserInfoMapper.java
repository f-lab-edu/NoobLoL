package com.nooblol.community.mapper;

import com.nooblol.community.dto.UserDto;
import com.nooblol.community.dto.UserInfoUpdateDto;
import com.nooblol.community.dto.UserLoginDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {

  int updateUserInfo(UserInfoUpdateDto userInfoUpdateDto);

  UserDto selectUser(UserLoginDto userLoginDto);
}
