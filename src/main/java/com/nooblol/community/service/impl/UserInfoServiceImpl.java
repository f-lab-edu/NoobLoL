package com.nooblol.community.service.impl;

import com.nooblol.community.dto.UserInfoUpdateDto;
import com.nooblol.community.mapper.UserInfoMapper;
import com.nooblol.community.service.UserInfoService;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.EncryptUtils;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

  private final UserInfoMapper userInfoMapper;

  @Override
  public ResponseDto updateUserInfo(UserInfoUpdateDto userInfoUpdateDto) {
    //두개가 모두 공백인 경우는 Update를 진행할 정보가 없기 떄문에 BadRequest를 반환한다.
    if (StringUtils.isBlank(userInfoUpdateDto.getNewUserName()) &&
        StringUtils.isBlank(userInfoUpdateDto.getNewPassword())
    ) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }

    try {
      userInfoUpdateDto.setOrgPassword(
          EncryptUtils.stringChangeToSha512(userInfoUpdateDto.getOrgPassword())
      );

      setNewPassword(userInfoUpdateDto);
      setNewUserName(userInfoUpdateDto);

      ResponseDto result = ResponseEnum.OK.getResponse();
      result.setResult(userInfoMapper.updateUserInfo(userInfoUpdateDto) > 0);
      return result;
    } catch (Exception e) {
      throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR, e);
    }
  }

  private void setNewPassword(UserInfoUpdateDto userInfoUpdateDto)
      throws UnsupportedEncodingException, NoSuchAlgorithmException {
    if (StringUtils.isBlank(userInfoUpdateDto.getNewPassword())) {
      userInfoUpdateDto.setNewPassword(userInfoUpdateDto.getOrgPassword());
      return;
    }
    userInfoUpdateDto.setNewPassword(
        EncryptUtils.stringChangeToSha512(userInfoUpdateDto.getNewPassword())
    );
  }

  private void setNewUserName(UserInfoUpdateDto userInfoUpdateDto) {
    if (StringUtils.isBlank(userInfoUpdateDto.getNewUserName())) {
      userInfoUpdateDto.setNewUserName(userInfoUpdateDto.getOrgUserName());
    }
  }


}

