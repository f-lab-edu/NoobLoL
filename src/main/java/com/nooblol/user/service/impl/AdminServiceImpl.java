package com.nooblol.user.service.impl;

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
import com.nooblol.global.utils.EncryptUtils;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final UserSignOutService userSignOutService;

  private final UserSignUpMapper userSignUpMapper;

  private final AdminMapper adminMapper;

  @Override
  public ResponseDto addAdminMember(UserSignUpRequestDto userSignUpRequestDto) {
    try {
      String password = EncryptUtils.stringChangeToSha512(userSignUpRequestDto.getPassword());
      userSignUpRequestDto.setPassword(password);

      userSignUpRequestDto.setAdminUserRole();

      ResponseDto returnDto = ResponseEnum.OK.getResponse();
      returnDto.setResult(userSignUpMapper.insertSignUpUser(userSignUpRequestDto) > 0);
      return returnDto;
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      throw new IllegalArgumentException(ExceptionMessage.SERVER_ERROR, e);
    } catch (DuplicateKeyException e) {
      throw new IllegalArgumentException(ExceptionMessage.HAVE_DATA, e);
    } catch (Exception e) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST, e);
    }
  }

  @Override
  public ResponseDto deleteAdminMember(UserSignOutDto userSignOutDto) {
    return userSignOutService.signOutUser(userSignOutDto);
  }

  @Override
  public ResponseDto forceDeleteUser(String deleteUserId) {
    ResponseDto resultDto = ResponseEnum.OK.getResponse();
    resultDto.setResult(adminMapper.forcedDeleteUser(deleteUserId) > 0);
    return resultDto;
  }

  @Override
  public ResponseDto getAllUserList(int pageNum, int limitNum) {
    List<UserDto> userList = adminMapper.getAllUserList(pageNum, limitNum);

    ResponseDto resultDto = ResponseEnum.OK.getResponse();
    resultDto.setResult(userList);

    return resultDto;
  }

  @Override
  public ResponseDto changeToActiveUser(String changeUserId) {
    return updateUser(changeUserId, UserRoleStatus.AUTH_USER.getRoleValue());
  }

  @Override
  public ResponseDto changeToSuspensionUser(String changeUserId) {
    return updateUser(changeUserId, UserRoleStatus.SUSPENSION_USER.getRoleValue());
  }

  private ResponseDto updateUser(String changeUserId, int roleStatus) {
    UserDto changeUser = new UserDto().builder()
        .userId(changeUserId)
        .userRole(roleStatus)
        .build();

    ResponseDto result = ResponseEnum.OK.getResponse();
    result.setResult(adminMapper.changeUserRole(changeUser) > 0);
    return result;
  }
}
