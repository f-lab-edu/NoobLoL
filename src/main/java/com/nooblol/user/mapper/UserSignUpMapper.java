package com.nooblol.user.mapper;

import com.nooblol.user.dto.UserSignUpRequestDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserSignUpMapper {

  int insertSignUpUser(UserSignUpRequestDto userDto);

  UserSignUpRequestDto selectUserInfoByEmail(String userEmail);

  UserSignUpRequestDto selectUserInfoByUserId(String userId);

  int updateUserRole(UserSignUpRequestDto userDto);
}
