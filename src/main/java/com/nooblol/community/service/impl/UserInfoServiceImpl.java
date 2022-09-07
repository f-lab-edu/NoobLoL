package com.nooblol.community.service.impl;

import com.nooblol.community.dto.UserInfoUpdateDto;
import com.nooblol.community.mapper.UserInfoMapper;
import com.nooblol.community.service.UserInfoService;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.EncryptUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserInfoMapper userInfoMapper;

  @Override
  public ResponseDto updateUserInfo(UserInfoUpdateDto userUpdateInfoDto) {
    //두개가 모두 공백인 경우는 Update를 진행할 정보가 없기 떄문에 BadRequest를 반환한다.
    if (StringUtils.isBlank(userUpdateInfoDto.getNewUserName()) &&
        StringUtils.isBlank(userUpdateInfoDto.getNewPassword())
    ) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }

    try {
      userUpdateInfoDto.setOrgPassword(
          EncryptUtils.stringChangeToSha512(userUpdateInfoDto.getOrgPassword())
      );

      //New Password의 경우 null이 존재 할 수 있다. 쿼리문에서 분기처리가 존재하기에 null이 와도 상관 없다.
      userUpdateInfoDto.setNewPassword(
          EncryptUtils.stringChangeToSha512(userUpdateInfoDto.getNewPassword())
      );

      ResponseDto result = ResponseEnum.OK.getResponse();
      if (userInfoMapper.updateUserInfo(userUpdateInfoDto) == 0) {
        result.setResult(false);
      } else {
        result.setResult(true);
      }
      return result;
    } catch (Exception e) {
      throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR);
    }
  }


}

