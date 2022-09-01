package com.nooblol.user.service;


import com.nooblol.user.dto.UserSignOutDto;
import com.nooblol.global.dto.ResponseDto;

public interface UserSignOutService {

  ResponseDto signOutUser(UserSignOutDto userSignOutDto);
}
