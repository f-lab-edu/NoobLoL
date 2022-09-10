package com.nooblol.community.service.impl;

import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.community.mapper.UserSignOutMapper;
import com.nooblol.community.service.UserSignOutService;
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
