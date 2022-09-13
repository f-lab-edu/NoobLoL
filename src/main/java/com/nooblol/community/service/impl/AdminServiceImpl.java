package com.nooblol.community.service.impl;

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
import com.nooblol.global.utils.EncryptUtils;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.servlet.http.HttpSession;
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
  public ResponseDto addAdminMember(
      UserSignUpRequestDto userSignUpRequestDto, HttpSession session
  ) {
    if (isSessionUserIsNotAdmin(session)) {
      throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
    }
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
  public ResponseDto deleteAdminMember(UserSignOutDto userSignOutDto, HttpSession session) {
    if (isSessionUserIsNotAdmin(session)) {
      throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
    }
    return userSignOutService.signOutUser(userSignOutDto);
  }

  /*
    TODO: [22. 09. 10] : 해당 부분에서 초기 생각은 요청자의 정보를 받아 DB에서 관리자가 맞는지 확인을 한 이후 작업하는 로직이었슴.
    다시 생각해보니 HttpSession에서 정보를 가져온 이후 대조하는게 더 맞는 판단으로 보이나,
    현재 Login정보가 담긴 기능이 제작되지 않은 상황. 로그인기능의 수정을 진행하면서 해당 영역의 수정이 필요함.
   */
  @Override
  public ResponseDto forceDeleteUser(String deleteUserId, HttpSession session) {
    if (isSessionUserIsNotAdmin(session)) {
      throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
    }

    ResponseDto resultDto = ResponseEnum.OK.getResponse();
    resultDto.setResult(adminMapper.forcedDeleteUser(deleteUserId) > 0);
    return resultDto;
  }

  /*
  TODO: [22. 09. 10] : 해당 부분에서 초기 생각은 요청자의 정보를 받아 DB에서 관리자가 맞는지 확인을 한 이후 작업하는 로직이었슴.
  다시 생각해보니 HttpSession에서 정보를 가져온 이후 대조하는게 더 맞는 판단으로 보이나,
  현재 Login정보가 담긴 기능이 제작되지 않은 상황. 로그인기능의 수정을 진행하면서 해당 영역의 수정이 필요함.

  사용자의 강제삭제 기능과 똑같이 로직수정이 필요함
 */
  @Override
  public ResponseDto getAllUserList(int pageNum, int limitNum, HttpSession session) {
    if (isSessionUserIsNotAdmin(session)) {
      throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
    }
    List<UserDto> userList = adminMapper.getAllUserList(pageNum, limitNum);

    ResponseDto resultDto = ResponseEnum.OK.getResponse();
    resultDto.setResult(userList);

    return resultDto;
  }

  @Override
  public ResponseDto changeToActiveUser(String changeUserId, HttpSession session) {
    isSessionUserIsNotAdmin(session);
    return updateUser(changeUserId, UserRoleStatus.AUTH_USER.getRoleValue());
  }

  @Override
  public ResponseDto changeToSuspensionUser(String changeUserId, HttpSession session) {
    isSessionUserIsNotAdmin(session);
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

  /**
   * 현재 로그인 중인 사용자가 관리자인지의 확인
   *
   * @param session
   * @return
   */
  private boolean isSessionUserIsNotAdmin(HttpSession session) {
    /**
     * TODO [22. 09. 10] : Session의 Login정보를 담는 기능이 이후에 개발되었다 보니 Git으로 가져오는 것보다는 로그인, 비로그인작업을 진행하면서 수정 예정
     * TODO [22. 09 .13] : 로그인 로그아웃시 AOP로 변경할것.
     */
    return false;
  }
}
