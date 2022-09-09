package com.nooblol.board.service;

import com.nooblol.board.dto.ArticleDto;
import com.nooblol.board.dto.LikeAndNotLikeResponseDto;
import javax.servlet.http.HttpSession;

public interface ArticleService {

  /**
   * 받은 articleId로 조회수를 증가한 이후 게시물정보를 반환한다.
   *
   * @param articleId 조회해야 하는 게시물 ID
   * @param userId    로그인을 한 경우, Session에 저장된 UserId
   * @return
   */
  ArticleDto getArticleInfo(int articleId, String userId);

  /**
   * 해당 사용자가 실제 사용자인지확인후, 게시물에서 행동할 수 있는 권한을 Return한다
   *
   * @param userId
   * @return
   */
  String getUserArticleAuth(String userId);

  /**
   * 받은 articleId의 조회수를 1 증가시킨다
   *
   * @param articleId
   */
  void addReadCount(int articleId);

  /**
   * 게시물의 삽입 또는 수정을 진행한다.
   *
   * @param articleDto
   * @param session
   * @param isInsert   삽입인 경우 True, Update인 경우 False
   * @return
   */
  boolean upsertArticle(ArticleDto articleDto, HttpSession session, boolean isInsert);

  /**
   * 현재 DB에서 사용주인 ArticleId의 최대값에 + 1을 하여 return한다. 만약 ArticleId가 없는 경우에는 1을 반환한다.
   *
   * @return
   */
  int getNewArticleId();

  /**
   * 게시물 삭제, 요청자가 관리자인 경우 또는 글 작성자인 경우에만 삭제를 진행한다.
   * Delete를 진행하는 경우 관련 테이블에 대해서도 삭제가 이뤄지다 보니, Transaction으로 묶어 처리를 진행한다
   *
   * @param articleId
   * @param session
   * @return
   */
  boolean deleteArticle(int articleId, HttpSession session);

  /**
   * 게시물 추천
   *
   * @param articleId
   * @param session   한개의 Article에 한번만 추천또는 비추천이 가능하며, 재요청이 들어온 경우 Delete처리를 하기위함.
   * @return
   */
  boolean likeArticle(int articleId, HttpSession session);

  /**
   * 게시물 비추천
   *
   * @param articleId
   * @param session   한개의 Article에 한번만 추천또는 비추천이 가능하며, 재요청이 들어온 경우 Delete처리를 하기위함.
   * @return
   */
  boolean notLikeArticle(int articleId, HttpSession session);

  /**
   * 해당 ArticleId의 좋아요 갯수와 싫어요 갯수를 Return한다.
   *
   * @param articleId
   * @return
   */
  LikeAndNotLikeResponseDto likeAndNotListStatus(int articleId);
}
