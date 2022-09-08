package com.nooblol.board.service;

import com.nooblol.board.dto.ArticleDto;
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
   * 게시물 Upsert
   *
   * @param articleDto
   * @return
   */
  boolean upsertArticle(ArticleDto articleDto, HttpSession session, boolean isUpdate);

  /**
   * 현재 DB에서 사용주인 ArticleId의 최대값에 + 1을 하여 return한다. 만약 ArticleId가 없는 경우에는 1을 반환한다.
   *
   * @return
   */
  int getNewArticleId();
}
