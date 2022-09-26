package com.nooblol.user.service.impl;

import com.nooblol.user.dto.UserSignOutDto;
import com.nooblol.user.mapper.UserSignOutMapper;
import com.nooblol.user.service.UserSignOutService;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.EncryptUtils;
import com.nooblol.global.utils.ResponseEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSignOutServiceImpl implements UserSignOutService {

  private final UserSignOutMapper userSignOutMapper;

  @Override
  public ResponseDto signOutUser(UserSignOutDto userSignOutDto) {
    try {
      String encodePassword = EncryptUtils.stringChangeToSha512(userSignOutDto.getPassword());
      userSignOutDto.setPassword(encodePassword);
    } catch (Exception e) {
      throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR, e);
    }

    if (userSignOutMapper.selectUserCount(userSignOutDto) == 0) {
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }

    ResponseDto rtnDto = ResponseEnum.OK.getResponse();
    rtnDto.setResult(userSignOutMapper.deleteUser(userSignOutDto) > 0);
    return rtnDto;
  }
}
