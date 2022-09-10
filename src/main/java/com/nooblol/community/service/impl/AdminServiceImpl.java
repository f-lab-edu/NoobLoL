package com.nooblol.community.service.impl;

import com.nooblol.community.dto.UserSignOutDto;
import com.nooblol.community.dto.UserSignUpRequestDto;
import com.nooblol.community.mapper.UserSignUpMapper;
import com.nooblol.community.service.AdminService;
import com.nooblol.community.service.UserSignOutService;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.EncryptUtils;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final UserSignOutService userSignOutService;

  private final UserSignUpMapper userSignUpMapper;

  @Override
  public ResponseDto addAdminMember(
      UserSignUpRequestDto userSignUpRequestDto, HttpSession session
  ) {

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
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }
  }

  @Override
  public ResponseDto deleteAdminMember(UserSignOutDto userSignOutDto, HttpSession session) {
    isSessionUserIsNotAdmin(session);
    return userSignOutService.signOutUser(userSignOutDto);
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
     */
    return true;
  }

}
