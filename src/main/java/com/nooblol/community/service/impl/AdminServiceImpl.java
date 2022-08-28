package com.nooblol.community.service.impl;

import com.nooblol.community.dto.AdminDeleteUserDto;
import com.nooblol.community.dto.AdminUserDto;
import com.nooblol.community.dto.UserDto;
import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.mapper.AdminMapper;
import com.nooblol.community.mapper.UserSignUpMapper;
import com.nooblol.community.service.AdminService;
import com.nooblol.community.service.UserSignOutService;
import com.nooblol.community.utils.UserRoleStatus;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.UserUtils;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserSignOutService userSignOutService;
  private final UserSignUpMapper userSignUpMapper;

  private final AdminMapper adminMapper;

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

  @Override
  public ResponseDto forceDeleteUser(AdminDeleteUserDto adminDeleteUserDto) {
    try {
      String adminPassword = UserUtils.stringChangeToSha512(
          adminDeleteUserDto.getAdminUserPassword()
      );
      adminDeleteUserDto.setAdminUserPassword(adminPassword);

      UserDto adminDbData = adminMapper.selectAdminDto(adminDeleteUserDto);

      if (ObjectUtils.isEmpty(adminDbData) ||
          adminDbData.getUserRole() != UserRoleStatus.ADMIN.getRoleValue()) {
        throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
      }

      ResponseDto resultDto = ResponseEnum.OK.getResponse();
      if (adminMapper.forcedDeleteUser(adminDeleteUserDto.getDeleteUserId()) == 0) {
        resultDto.setResult(false);
      } else {
        resultDto.setResult(true);
      }
      return resultDto;
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR);
    }
  }

  @Override
  public ResponseDto getAllUserList(AdminUserDto adminUserDto) {
    try {
      String password = UserUtils.stringChangeToSha512(adminUserDto.getAdminUserPassword());
      adminUserDto.setAdminUserPassword(password);

      UserDto adminDbData = adminMapper.selectAdminDto(adminUserDto);

      if (ObjectUtils.isEmpty(adminDbData) ||
          adminDbData.getUserRole() != UserRoleStatus.ADMIN.getRoleValue()) {
        throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
      }

      List<UserDto> userList = adminMapper.getAllUserList();

      ResponseDto resultDto = ResponseEnum.OK.getResponse();
      resultDto.setResult(userList);

      return resultDto;
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR);
    } catch (Exception e) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }
  }


}
