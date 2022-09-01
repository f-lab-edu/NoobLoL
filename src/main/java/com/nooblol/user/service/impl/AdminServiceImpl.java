package com.nooblol.user.service.impl;

import com.nooblol.user.dto.AdminDeleteUserDto;
import com.nooblol.user.dto.AdminUpdateUserDto;
import com.nooblol.user.dto.AdminUserDto;
import com.nooblol.user.dto.UserDto;
import com.nooblol.user.dto.UserSignOutDto;
import com.nooblol.user.dto.UserSignUpRequestDto;
import com.nooblol.user.mapper.AdminMapper;
import com.nooblol.user.mapper.UserSignUpMapper;
import com.nooblol.user.service.AdminService;
import com.nooblol.user.service.UserSignOutService;
import com.nooblol.user.utils.UserRoleStatus;
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
    } catch (Exception e) {
      if (e.getMessage().equals(ExceptionMessage.FORBIDDEN)) {
        throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
      }
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
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
      if (e.getMessage().equals(ExceptionMessage.FORBIDDEN)) {
        throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
      }
      e.printStackTrace();
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }
  }

  @Override
  public ResponseDto changeToActiveUser(AdminUpdateUserDto adminUpdateUserDto) {
    return updateUser(adminUpdateUserDto, UserRoleStatus.AUTH_USER.getRoleValue());
  }

  @Override
  public ResponseDto changeToSuspensionUser(AdminUpdateUserDto adminUpdateUserDto) {
    return updateUser(adminUpdateUserDto, UserRoleStatus.SUSPENSION_USER.getRoleValue());
  }

  private ResponseDto updateUser(AdminUpdateUserDto adminUpdateUserDto, int roleStatus) {
    try {
      String password = UserUtils.stringChangeToSha512(adminUpdateUserDto.getAdminUserPassword());
      adminUpdateUserDto.setAdminUserPassword(password);

      UserDto adminDbData = adminMapper.selectAdminDto(adminUpdateUserDto);

      if (ObjectUtils.isEmpty(adminDbData) ||
          adminDbData.getUserRole() != UserRoleStatus.ADMIN.getRoleValue()) {
        throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
      }

      UserDto changeUser = new UserDto();
      changeUser.setUserId(adminUpdateUserDto.getUpdateUserId());
      changeUser.setUserRole(roleStatus);

      ResponseDto result = ResponseEnum.OK.getResponse();

      if (adminMapper.changeUserRole(changeUser) == 0) {
        result.setResult(false); //수정할 데이터가 없는 경우 (이미 활동정지가 된 유저)
      } else {
        result.setResult(true);
      }
      return result;
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR);
    } catch (Exception e) {
      if (e.getMessage().equals(ExceptionMessage.FORBIDDEN)) {
        throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
      }
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }
  }
}
