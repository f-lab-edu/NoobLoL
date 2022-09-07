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
}
