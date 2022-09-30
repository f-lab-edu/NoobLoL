package com.nooblol.board.service;

import com.nooblol.board.dto.LikeAndNotLikeResponseDto;
import javax.servlet.http.HttpSession;

public interface ArticleStatusService {

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
