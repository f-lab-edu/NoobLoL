package com.nooblol.user.service.impl;

import com.nooblol.user.dto.LetterDto;
import com.nooblol.user.dto.LetterInsertRequestDto;
import com.nooblol.user.dto.LetterSearchDto;
import com.nooblol.user.mapper.LetterMapper;
import com.nooblol.user.service.LetterService;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService {

  private final LetterMapper letterMapper;

  @Override
  public LetterDto getLetter(int letterId, HttpSession session) {
    return null;
  }

  @Override
  public List<LetterDto> getLetterListByLetterId(LetterSearchDto letterSearchDto) {
    return null;
  }

  @Override
  public boolean insertLetter(LetterInsertRequestDto letterInsertRequestDto, HttpSession session) {
    return false;
  }

  @Override
  public boolean deleteLetter(LetterDto letterDto, HttpSession session) {
    return false;
  }

  /**
   * ToStatus 또는 FromStatus 값을 Update한다
   *
   * @param letterId 대상 쪽지Id
   * @param status   변경할 StatusValue
   * @param type     발신자 Status 인지 수신자 Status인지를 구분한다
   * @return
   */
  private boolean updateLetterStatus(int letterId, int status, String type) {
    return false;
  }
}
