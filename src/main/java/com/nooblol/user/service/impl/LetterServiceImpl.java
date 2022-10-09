package com.nooblol.user.service.impl;

import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionUtils;
import com.nooblol.user.dto.LetterDto;
import com.nooblol.user.dto.LetterInsertRequestDto;
import com.nooblol.user.dto.LetterSearchDto;
import com.nooblol.user.mapper.LetterMapper;
import com.nooblol.user.service.LetterService;
import com.nooblol.user.service.UserInfoService;
import com.nooblol.user.utils.LetterConstants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService {

  private final LetterMapper letterMapper;

  private final UserInfoService userInfoService;

  @Override
  public LetterDto getLetter(int letterId, HttpSession session) {
    LetterDto letterDto = letterMapper.selectLetterByLetterId(letterId);
    String letterReqUserId = SessionUtils.getSessionUserId(session);

    if (ObjectUtils.isEmpty(letterDto)) {
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }

    if (!ObjectUtils.isEmpty(letterDto) &&
        !(letterReqUserId.equals(letterDto.getToUserId()) ||
            letterReqUserId.equals(letterDto.getFromUserId()))
    ) {
      throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
    }

    //발송자인 경우에는 먼저 return
    if (letterReqUserId.equals(letterDto.getFromUserId())) {
      return letterDto;
    }
    //수신자의 경우 읽지 않았으면 Update
    if (letterDto.getToStatus() == LetterConstants.LETTER_STATUS_UNREAD) {
      letterDto.setToStatus(LetterConstants.LETTER_STATUS_READ);
      updateLetterStatus(
          letterDto.getLetterId(),
          LetterConstants.LETTER_STATUS_READ,
          LetterConstants.LETTER_TYPE_TO,
          session
      );
    }

    return letterDto;
  }

  @Override
  public List<LetterDto> getLetterListByUserId(LetterSearchDto letterSearchDto) {
    if (LetterConstants.LETTER_TYPE_TO.equals(letterSearchDto.getLetterType())) {
      return Optional.ofNullable(
          letterMapper.selectLetterListByUserIdAndTypeTo(letterSearchDto)
      ).orElse(new ArrayList<LetterDto>());
    }

    if (LetterConstants.LETTER_TYPE_FROM.equals(letterSearchDto.getLetterType())) {
      return Optional.ofNullable(
          letterMapper.selectLetterListByUserIdAndTypeFrom(letterSearchDto)
      ).orElse(new ArrayList<LetterDto>());
    }

    throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
  }

  @Override
  public boolean insertLetter(LetterInsertRequestDto requestDto, HttpSession session) {
    String fromUserId = SessionUtils.getSessionUserId(session);
    if (requestDto.getToUserId().equals(fromUserId)) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }

    //실제 수신자가 존재하지 않는지 여부 확인
    if (Optional
        .ofNullable(userInfoService.selectUserInfoByUserId(requestDto.getToUserId()))
        .isEmpty()) {
      throw new IllegalArgumentException(ExceptionMessage.NOT_FOUND);
    }

    LetterDto insertLetter = LetterDto.builder()
        .letterTitle(requestDto.getLetterTitle())
        .letterContent(requestDto.getLetterContent())
        .toUserId(requestDto.getToUserId())
        .toStatus(LetterConstants.LETTER_STATUS_UNREAD)
        .fromUserId(fromUserId)
        .fromStatus(LetterConstants.LETTER_STATUS_READ)
        .createdAt(LocalDateTime.now())
        .build();

    return letterMapper.insertLetter(insertLetter) > 0;
  }

  @Override
  public boolean deleteLetter(LetterDto letterDto, HttpSession session) {
    return updateLetterStatus(
        letterDto.getLetterId(),
        LetterConstants.LETTER_STATUS_DELETE,
        letterDto.getType(),
        session
    );
  }

  /**
   * ToStatus 또는 FromStatus 값을 Update한다
   *
   * @param letterId
   * @param status
   * @param letterType
   * @param session
   * @return
   */
  private boolean updateLetterStatus(
      int letterId, int status, String letterType, HttpSession session
  ) {
    letterType = letterType.toUpperCase();

    if (LetterConstants.LETTER_TYPE_TO.equals(letterType)) {
      return letterMapper.updateLetterToStatusByLetterIdAndToUserId(
          LetterDto.builder()
              .letterId(letterId)
              .toStatus(status)
              .toUserId(SessionUtils.getSessionUserId(session))
              .build()
      ) > 0;
    }

    if (LetterConstants.LETTER_TYPE_FROM.equals(letterType)) {
      return letterMapper.updateLetterFromStatusByLetterIdAndFromUserId(
          LetterDto.builder()
              .letterId(letterId)
              .fromStatus(status)
              .fromUserId(SessionUtils.getSessionUserId(session))
              .build()
      ) > 0;
    }

    //LetterConstants에 없는 Type(수신 또는 발신이 아닌경우) Exception
    throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
  }
}
