package com.nooblol.community.mapper;

import com.nooblol.community.dto.UserSignUpRequestDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserSignUpMapper {

  int insertSignUpUser(UserSignUpRequestDto userDto);

  UserSignUpRequestDto selectUserInfoByEmail(String userEmail);

  int updateUserRole(UserSignUpRequestDto userDto);
}
