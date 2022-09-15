package com.nooblol.user.mapper;

import com.nooblol.user.dto.UserDto;
import com.nooblol.user.dto.UserInfoUpdateDto;
import com.nooblol.user.dto.UserLoginDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {

  int updateUserInfo(UserInfoUpdateDto userInfoUpdateDto);

  UserDto selectUser(UserLoginDto userLoginDto);

  UserDto selectUserByUserId(String userId);
}
