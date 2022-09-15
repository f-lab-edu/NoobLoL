package com.nooblol.user.service;

import com.nooblol.user.dto.LetterDto;
import com.nooblol.user.dto.LetterInsertRequestDto;
import com.nooblol.user.dto.LetterSearchDto;
import java.util.List;
import javax.servlet.http.HttpSession;

public interface LetterService {

  /**
   * 파라미터로 받은 letterId의 쪽지의 발송자 또는 수신자인 경우 쪽지의 내용을 Return한다.
   *
   * @param letterId
   * @param session
   * @return
   */
  LetterDto getLetter(int letterId, HttpSession session);

  /**
   * 쪽지의 최근 리스트를 반환하며, Type값에 따라 수신리스트인지 발신리스트인지를 구분한다.
   *
   * @param letterSearchDto
   * @return
   */
  List<LetterDto> getLetterListByUserId(LetterSearchDto letterSearchDto);


  /**
   * 쪽지의 발송기능이며, 본인이 본인한테의 쪽지 발송은 불가능하다.
   *
   * @param requestDto
   * @param session
   * @return
   */
  boolean insertLetter(LetterInsertRequestDto requestDto, HttpSession session);

  /**
   * 쪽지의 삭제, 실제로는 DB에서 Status값을 Update 처리만 한다.
   *
   * @param letterDto
   * @param session
   * @return
   */
  boolean deleteLetter(LetterDto letterDto, HttpSession session);

}
