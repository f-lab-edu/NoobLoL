package com.nooblol.user.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionSampleObject;
import com.nooblol.global.utils.SessionUtils;
import com.nooblol.user.dto.LetterDto;
import com.nooblol.user.dto.LetterInsertRequestDto;
import com.nooblol.user.dto.LetterSearchDto;
import com.nooblol.user.dto.UserDto;
import com.nooblol.user.mapper.LetterMapper;
import com.nooblol.user.service.UserInfoService;
import com.nooblol.user.utils.LetterConstants;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LetterServiceImplTest {

  @InjectMocks
  LetterServiceImpl letterService;

  @Mock
  LetterMapper letterMapper;

  @Mock
  UserInfoService userInfoService;

  HttpSession authUserSession = SessionSampleObject.authUserLoginSession;


  @Order(1)
  @Nested
  @DisplayName("쪽지 발송 테스트")
  class LetterInsertTest {

    @Test
    @DisplayName("쪽지의 수신인이 발송인과 동일할 경우 BadRequest Exception이 발생한다")
    void insertLetter_WhenToUserEqualsFromUser_ThenBadRequestException() {
      //given
      LetterInsertRequestDto requestDto = new LetterInsertRequestDto();
      requestDto.setToUserId(SessionUtils.getSessionUserId(authUserSession));

      //when
      Exception e = assertThrows(IllegalArgumentException.class, () -> {
        letterService.insertLetter(requestDto, authUserSession);
      });

      //then
      assertEquals(e.getMessage(), ExceptionMessage.BAD_REQUEST);
    }

    @Test
    @DisplayName("쪽지의 수신인이 실제 DB에 없는 UserId인 경우 NotFound Exception이 발생한다.")
    void insertLetter_WhenNotExistsToUser_ThenNotFoundException() {
      //given
      LetterInsertRequestDto requestDto = new LetterInsertRequestDto();
      requestDto.setToUserId("NotExists User Id ");

      //mock
      when(userInfoService.selectUserInfoByUserId(requestDto.getToUserId())).thenReturn(null);

      //when
      Exception e = assertThrows(IllegalArgumentException.class, () -> {
        letterService.insertLetter(requestDto, authUserSession);
      });

      //then
      assertEquals(e.getMessage(), ExceptionMessage.NOT_FOUND);
    }

    @Test
    @DisplayName("본인한테 보낸 쪽지가아니고, 수신인이 존재하는 경우 Return으로 true를 획득한다.")
    void insertLetter_WhenInsertSuccess_ThenReturnTrue() {
      //given
      String mockToUserId = "real User Id";

      LetterInsertRequestDto mockRequestDto = new LetterInsertRequestDto().builder()
          .letterTitle("Sample Title")
          .letterContent("Sample Content")
          .toUserId(mockToUserId)
          .build();

      UserDto mockReturnDto = new UserDto();

      //mock
      when(userInfoService.selectUserInfoByUserId(mockRequestDto.getToUserId()))
          .thenReturn(mockReturnDto);
      when(letterMapper.insertLetter(any())).thenReturn(1);

      //when
      boolean result = letterService.insertLetter(mockRequestDto, authUserSession);

      //then
      assertTrue(result);
    }


  }

  @Order(2)
  @Nested
  @DisplayName("쪽지 조회 테스트")
  class LetterSelectTest {

    @Test
    @DisplayName("조회시 데이터가 없는 경우 NotFound Exception이 밸생한다.")
    void getLetter_WhenNoHaveLetter_ThenNotFoundException() {
      //given
      int nullLetterId = 99999;

      //mock
      when(letterMapper.selectLetterByLetterId(nullLetterId)).thenReturn(null);

      //when
      Exception e = assertThrows(IllegalArgumentException.class, () -> {
        letterService.getLetter(nullLetterId, authUserSession);
      });

      //then
      assertEquals(e.getMessage(), ExceptionMessage.NO_DATA);
    }

    @Test
    @DisplayName("조회시 수신자와 발신자의 UserId가 모두 현재 로그인한 사용자가 아닌 경우 Forbidden Exception이 발생한다. ")
    void getLetter_WhenReqUserNotToUserIdAndFromUserId_ThenForbiddenException() {
      //given
      int letterId = 1;

      LetterDto mockReturnLetterDto = new LetterDto().builder()
          .letterId(letterId)
          .toUserId("Don't have User Id")
          .fromUserId("Don't have User Id")
          .build();

      //mock
      when(letterMapper.selectLetterByLetterId(letterId)).thenReturn(mockReturnLetterDto);

      //when
      Exception e = assertThrows(IllegalArgumentException.class, () -> {
        letterService.getLetter(letterId, authUserSession);
      });

      //then
      assertEquals(e.getMessage(), ExceptionMessage.FORBIDDEN);
    }

    @Test
    @DisplayName("수신자 또는 발신자인 경우에는 정상적으로 쪽지 데이터를 획득한다")
    void getLetter_WhenReqUserIsToUserIdOrFromUserId_ThenReturnLetter() {
      //given
      int letterId = 1;

      LetterDto mockReturnLetterDto = new LetterDto().builder()
          .letterId(letterId)
          .toUserId("Don't have User Id")
          .fromUserId(SessionUtils.getSessionUserId(authUserSession))
          .build();

      //mock
      when(letterMapper.selectLetterByLetterId(letterId)).thenReturn(mockReturnLetterDto);

      //when
      LetterDto result = letterService.getLetter(letterId, authUserSession);

      //then
      assertEquals(mockReturnLetterDto, result);
    }
  }

  @Order(3)
  @Nested
  @DisplayName("쪽지 리스트 조회 테스트")
  class LetterListSelectTest {

    @Test
    @DisplayName("수신, 발신 리스트 타입이 아닌 다른 타입이 들어온 경우, BadRequest가 발생한다.")
    void getLetterListByUserId_WhenIsNotExistsType_ThenBadRequestExcpeiton() {
      //gvien
      LetterSearchDto requestDto = new LetterSearchDto().builder()
          .letterType("없는 타입")
          .build();

      //when
      Exception e = assertThrows(IllegalArgumentException.class,
          () -> letterService.getLetterListByUserId(requestDto)
      );

      //then
      assertEquals(e.getMessage(), ExceptionMessage.BAD_REQUEST);
    }

    @Test
    @DisplayName("수신 리스트 조회시에 데이터가 없는 경우, 비어있는 리스트를 획득한다.")
    void getLetterListByUserId_WhenIsNotExistsLetterByDbTypeTo_ThenReturnEmptyLetterList() {
      //given
      LetterSearchDto requestDto = new LetterSearchDto().builder()
          .letterType(LetterConstants.LETTER_TYPE_TO)
          .build();

      //mock
      when(letterMapper.selectLetterListByUserIdAndTypeTo(requestDto)).thenReturn(null);

      //when
      List<LetterDto> result = letterService.getLetterListByUserId(requestDto);

      //then
      assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("수신 리스트 조회시 데이터가 있는 경우, 수신된 쪽지 리스트를 획득한다.")
    void getLetterListByUserId_WhenIsExistsLetterAndTypeTo_ThenReturnLetterList() {
      //given
      LetterSearchDto requestDto = new LetterSearchDto().builder()
          .userId(SessionUtils.getSessionUserId(authUserSession))
          .letterType(LetterConstants.LETTER_TYPE_TO)
          .statusArr(LetterConstants.LETTER_LIST_SEARCH_STATUS_ARR)
          .limitNum(30)
          .pageNum(0)
          .build();

      LetterDto letterListObj1 = new LetterDto().builder()
          .letterId(1)
          .build();
      LetterDto letterListObj2 = new LetterDto().builder()
          .letterId(2)
          .build();
      LetterDto letterListObj3 = new LetterDto().builder()
          .letterId(3)
          .build();

      List<LetterDto> mockReturnLetterList = new ArrayList<>();
      mockReturnLetterList.add(letterListObj1);
      mockReturnLetterList.add(letterListObj2);
      mockReturnLetterList.add(letterListObj3);

      //mock
      when(letterMapper.selectLetterListByUserIdAndTypeTo(requestDto)).thenReturn(
          mockReturnLetterList);

      //when
      List<LetterDto> result = letterService.getLetterListByUserId(requestDto);

      //then
      assertEquals(result, mockReturnLetterList);
    }

    @Test
    @DisplayName("발송 리스트 조회시에 데이터가 없는 경우, 비어있는 리스트를 획득한다.")
    void getLetterListByUserId_WhenIsNotExistsLetterByDbTypeFrom_ThenReturnEmptyLetterList() {
      //given
      LetterSearchDto requestDto = new LetterSearchDto().builder()
          .letterType(LetterConstants.LETTER_TYPE_FROM)
          .build();

      //mock
      when(letterMapper.selectLetterListByUserIdAndTypeFrom(requestDto)).thenReturn(null);

      //when
      List<LetterDto> result = letterService.getLetterListByUserId(requestDto);

      //then
      assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("발송 리스트 조회시 데이터가 있는 경우, 발송한 쪽지 리스트를 획득한다.")
    void getLetterListByUserId_WhenIsExistsLetterAndTypeFrom_ThenReturnLetterList() {
      //given
      LetterSearchDto requestDto = new LetterSearchDto().builder()
          .userId(SessionUtils.getSessionUserId(authUserSession))
          .letterType(LetterConstants.LETTER_TYPE_FROM)
          .statusArr(LetterConstants.LETTER_LIST_SEARCH_STATUS_ARR)
          .limitNum(30)
          .pageNum(0)
          .build();

      LetterDto letterListObj1 = new LetterDto().builder()
          .letterId(1)
          .build();
      LetterDto letterListObj2 = new LetterDto().builder()
          .letterId(2)
          .build();
      LetterDto letterListObj3 = new LetterDto().builder()
          .letterId(3)
          .build();

      List<LetterDto> mockReturnLetterList = new ArrayList<>();
      mockReturnLetterList.add(letterListObj1);
      mockReturnLetterList.add(letterListObj2);
      mockReturnLetterList.add(letterListObj3);

      //mock
      when(letterMapper.selectLetterListByUserIdAndTypeFrom(requestDto)).thenReturn(
          mockReturnLetterList);

      //when
      List<LetterDto> result = letterService.getLetterListByUserId(requestDto);

      //then
      assertEquals(result, mockReturnLetterList);
    }
  }


  @Order(4)
  @Nested
  @DisplayName("쪽지 삭제 테스트")
  class LetterDeleteTest {

    @Test
    @DisplayName("Constants에 없는 Type이 입력된 경우 BadRequest Exception이 발생한다.")
    void deleteLetter_WhenLetterTypeNotExistsConstants_ThenBadRequestException() {
      //given
      LetterDto requestDto = new LetterDto();
      requestDto.setType("Not Exists Constants Type");

      //when
      Exception e = assertThrows(IllegalArgumentException.class, () -> {
        letterService.deleteLetter(requestDto, authUserSession);
      });

      //then
      assertEquals(e.getMessage(), ExceptionMessage.BAD_REQUEST);
    }

    @Test
    @DisplayName("DB에 letterId의 쪽지가 없는 경우 Return으로 False를 획득한다.")
    void deleteLetter_WhenIsNotExistsLetter_ThenReturnFalse() {
      //given
      int nullLetterId = 99999;
      LetterDto requestDto = new LetterDto();
      requestDto.setLetterId(nullLetterId);
      requestDto.setType(LetterConstants.LETTER_TYPE_TO);

      //mock
      when(letterMapper.updateLetterToStatusByLetterIdAndToUserId(any())).thenReturn(0);

      //when
      boolean result = letterService.deleteLetter(requestDto, authUserSession);

      //then
      assertFalse(result);
    }

    @Test
    @DisplayName("DB에 letterId의 쪽지가 존재 하는 경우 Return으로 True를 획득한다.")
    void deleteLetter_WhenIsExistsLetter_ThenReturnTrue() {
      //given
      int nullLetterId = 99999;
      LetterDto requestDto = new LetterDto();
      requestDto.setLetterId(nullLetterId);
      requestDto.setType(LetterConstants.LETTER_TYPE_TO);

      //mock
      when(letterMapper.updateLetterToStatusByLetterIdAndToUserId(any())).thenReturn(1);

      //when
      boolean result = letterService.deleteLetter(requestDto, authUserSession);

      //then
      assertTrue(result);
    }

  }
}