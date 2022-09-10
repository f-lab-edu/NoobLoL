package com.nooblol.board.service;

import com.nooblol.board.dto.ReplyDto;
import com.nooblol.board.dto.ReplyRequestDto.ReplyInsertDto;
import com.nooblol.board.dto.ReplyRequestDto.ReplyUpdateDto;
import javax.servlet.http.HttpSession;

public interface ArticleReplyService {

  /**
   * 댓글 추가
   *
   * @param insertDto
   * @param session
   * @return
   */
  boolean insertReply(ReplyInsertDto insertDto, HttpSession session);

  /**
   * 댓글의 수정, 요청자가 작성자인 경우 또는 관리자인 경우에 해당 기능이 실행된다
   *
   * @param updateDto
   * @param session
   * @return
   */
  boolean updateReply(ReplyUpdateDto updateDto, HttpSession session);

  /**
   * 댓글의 Insert, Update를 진행한다
   *
   * @param replyDto
   * @return
   */
  int upsertReply(ReplyDto replyDto);

  /**
   * 댓글의 삭제, 요청자가 작성자인 경우이거나 또는 관리자인 경우에 해당 기능이 실행된다.
   *
   * @param replyId
   * @param session
   * @return
   */
  boolean deleteReplyByReplyId(int replyId, HttpSession session);

}
