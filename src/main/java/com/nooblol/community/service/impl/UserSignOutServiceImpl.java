package com.nooblol.community.service.impl;

import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.community.mapper.UserSignOutMapper;
import com.nooblol.community.service.UserSignOutService;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.UserUtils;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignOutServiceImpl implements UserSignOutService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserSignOutMapper userSignOutMapper;

  @Override
  public ResponseDto signOutUser(UserSignOutDto userSignOutDto) {
    try {
      String encodePassword = UserUtils.stringChangeToSha512(userSignOutDto.getPassword());
      userSignOutDto.setPassword(encodePassword);
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR);
    }

    if (userSignOutMapper.selectUserCount(userSignOutDto) == 0) {
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }

    ResponseDto rtnDto = ResponseEnum.OK.getResponse();
    if (userSignOutMapper.deleteUser(userSignOutDto) == 0) {
      rtnDto.setResult(false);
      return rtnDto;
    }

    rtnDto.setResult(true);
    return rtnDto;
  }
}
