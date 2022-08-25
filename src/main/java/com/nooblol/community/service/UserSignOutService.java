package com.nooblol.community.service;


import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.global.dto.ResponseDto;

public interface UserSignOutService {

  ResponseDto signOutUser(UserSignOutDto userSignOutDto);
}
