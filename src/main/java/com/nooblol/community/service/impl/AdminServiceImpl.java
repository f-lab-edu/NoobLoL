package com.nooblol.community.service.impl;

import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.mapper.UserSignUpMapper;
import com.nooblol.community.service.AdminService;
import com.nooblol.community.service.UserSignOutService;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.UserUtils;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserSignOutService userSignOutService;
  private final UserSignUpMapper userSignUpMapper;

  @Override
  public ResponseDto addAdminMember(UserSignUpRequestDto userSignUpRequestDto) {
    try {
      String password = UserUtils.stringChangeToSha512(userSignUpRequestDto.getPassword());
      userSignUpRequestDto.setPassword(password);

      userSignUpRequestDto.setAdminUserRole();

      ResponseDto returnDto = ResponseEnum.OK.getResponse();
      if (userSignUpMapper.insertSignUpUser(userSignUpRequestDto) == 0) {
        returnDto.setResult(false);
      } else {
        returnDto.setResult(true);
      }
      return returnDto;
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR);
    } catch (Exception e) {
      if (e instanceof DuplicateKeyException) {
        SQLException se = (SQLException) e.getCause();
        if (se.getErrorCode() == 23505) {
          throw new IllegalArgumentException(ExceptionMessage.HAVE_DATA);
        }
      }
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }
  }

  @Override
  public ResponseDto deleteAdminMember(UserSignOutDto userSignOutDto) {
    return userSignOutService.signOutUser(userSignOutDto);
  }


}
