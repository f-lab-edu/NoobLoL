package com.nooblol.user.service.impl;

import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionUtils;
import com.nooblol.user.dto.LetterDto;
import com.nooblol.user.dto.LetterInsertRequestDto;
import com.nooblol.user.dto.LetterSearchDto;
import com.nooblol.user.mapper.LetterMapper;
import com.nooblol.user.service.LetterService;
import com.nooblol.user.utils.LetterConstants;
import java.util.Arrays;
import java.util.List;
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

  @Override
  public LetterDto getLetter(int letterId, HttpSession session) {
    LetterDto letterDto = letterMapper.selectLetterByLetterId(letterId);
    String letterReqUserId = SessionUtils.getSessionUserId(session);

    if (ObjectUtils.isEmpty(letterDto)) {
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }

    //발송자인 경우에는 먼저 return
    if (letterReqUserId.equals(letterDto.getFromUserId())) {
      return letterDto;
    }

    //수신자의 경우에만 Update
    updateLetterStatus(
        letterDto.getLetterId(),
        LetterConstants.LETTER_STATUS_READ,
        LetterConstants.LETTER_TYPE_TO,
        session
    );

    return letterDto;
  }

  @Override
  public List<LetterDto> getLetterListByLetterId(LetterSearchDto letterSearchDto) {
    if (LetterConstants.LETTER_TYPE_TO.equals(letterSearchDto.getLetterType())) {
      return letterMapper.selectLetterListByLetterIdAndTypeTo(letterSearchDto);
    }

    if (LetterConstants.LETTER_TYPE_FROM.equals(letterSearchDto.getLetterType())) {
      return letterMapper.selectLetterListByLetterIdAndTypeFrom(letterSearchDto);
    }

    throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
  }

  @Override
  public boolean insertLetter(LetterInsertRequestDto requestDto, HttpSession session) {
    String fromUserId = SessionUtils.getSessionUserId(session);
    if (requestDto.getToUserId().equals(fromUserId)) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }

    LetterDto insertLetter = new LetterDto().builder()
        .letterTitle(requestDto.getLetterTitle())
        .letterContent(requestDto.getLetterContent())
        .toUserId(requestDto.getToUserId())
        .toStatus(LetterConstants.LETTER_STATUS_UNREAD)
        .fromUserId(fromUserId)
        .fromStatus(LetterConstants.LETTER_STATUS_READ)
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
    if (!statusValid(status)) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }

    //혹시라도 String으로 작성하는경우를 대비해 toUpperCase를 진행해준다.
    letterType = letterType.toUpperCase();

    if (LetterConstants.LETTER_TYPE_TO.equals(letterType)) {
      return letterMapper.updateLetterToStatusByLetterIdAndToUserId(
          new LetterDto().builder()
              .letterId(letterId)
              .toStatus(status)
              .toUserId(SessionUtils.getSessionUserId(session))
              .build()
      ) > 0;
    }

    if (LetterConstants.LETTER_TYPE_FROM.equals(letterType)) {
      return letterMapper.updateLetterFromStatusByLetterIdAndFromUserId(
          new LetterDto().builder()
              .letterId(letterId)
              .fromStatus(status)
              .fromUserId(SessionUtils.getSessionUserId(session))
              .build()
      ) > 0;
    }

    throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
  }

  /**
   * Status의 값이 실제 Constants에 존재하는지 여부 확인
   * @param status
   * @return
   */
  private boolean statusValid(int status) {
    return Arrays.stream(LetterConstants.LETTER_LIST_STATUS_ARR)
        .anyMatch(arrStatusVal -> arrStatusVal == status);
  }

}
