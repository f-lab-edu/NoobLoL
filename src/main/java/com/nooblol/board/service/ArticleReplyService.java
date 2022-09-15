package com.nooblol.board.service;

import com.nooblol.board.dto.ReplyDto;
import com.nooblol.board.dto.ReplyRequestDto.ReplyInsertDto;
import com.nooblol.board.dto.ReplyRequestDto.ReplyUpdateDto;
import java.util.List;
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

  /**
   * 단건의 댓글 조회
   *
   * @param replyId
   * @return
   */
  ReplyDto selectReplyByReplyId(int replyId);

  /**
   * 댓글 리스트 조회
   *
   * @param articleId
   * @return
   */
  // TODO [22. 09. 15] : 보통 댓글의 경우에는 타 웹사이트들에서도 댓글을 한번에 볼 수 있는 경우가 많아 Limit를 따로 하지 않았으나 해야하나 고민 됨.
  List<ReplyDto> selectReplyListByArticleId(int articleId);

}
