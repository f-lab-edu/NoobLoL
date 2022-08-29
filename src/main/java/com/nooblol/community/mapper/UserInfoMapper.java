package com.nooblol.community.mapper;

import com.nooblol.community.dto.UserInfoUpdateDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {

  int updateUserInfo(UserInfoUpdateDto userInfoUpdateDto);
}
