package com.nooblol.board.utils;

import com.nooblol.board.dto.ArticleDto;
import com.nooblol.board.dto.ArticleInsertRequestDto;
import java.time.LocalDateTime;

public class ArticleFixtureUtils {


  /**
   * 로그인을 하지 않은 경우에 반환되는 게시물 정보
   *
   * @param articleId     설정할 articleId
   * @param createdUserId 가정으로 잡을 생성자 UserId
   * @return
   */
  public static ArticleDto guestActiveArticleFixture(int articleId, String createdUserId) {
    return new ArticleDto().builder()
        .articleId(articleId)
        .bbsId(1)
        .articleTitle("Sample Article Title")
        .articleContent("Sample Article Content")
        .articleReadCount(5)
        .status(ArticleStatusEnum.ACTIVE.getStatus())
        .createdUserId(createdUserId)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .authMessage(ArticleAuthMessage.GUEST.name())
        .build();
  }

  /**
   * 로그인한 사용자가 작성자 혹은 일반 사용자인 경우 반환되는 게시물 정보
   *
   * @param articleId     설정할 articleId
   * @param createdUserId 가정으로 잡을 생성자 UserId
   * @return
   */
  public static ArticleDto userActiveArticleFixture(int articleId, String createdUserId) {
    return new ArticleDto().builder()
        .articleId(articleId)
        .bbsId(1)
        .articleTitle("Sample Article Title")
        .articleContent("Sample Article Content")
        .articleReadCount(6)
        .status(ArticleStatusEnum.ACTIVE.getStatus())
        .createdUserId(createdUserId)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .authMessage(ArticleAuthMessage.USER.name())
        .build();
  }

  /**
   * 로그인한 사용자가 관리자인 경우 반환되는 게시물 정보
   *
   * @param articleId
   * @param createdUserId
   * @return
   */
  public static ArticleDto adminActiveArticleFixture(int articleId, String createdUserId) {
    return new ArticleDto().builder()
        .articleId(articleId)
        .bbsId(1)
        .articleTitle("Sample Article Title")
        .articleContent("Sample Article Content")
        .articleReadCount(9)
        .status(ArticleStatusEnum.ACTIVE.getStatus())
        .createdUserId(createdUserId)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .authMessage(ArticleAuthMessage.ADMIN.name())
        .build();
  }


  public static ArticleInsertRequestDto insertArticleFixture(String createdUserId) {
    ArticleInsertRequestDto result = new ArticleInsertRequestDto();
    result.setBbsId(1);
    result.setArticleTitle("Sample Article Title");
    result.setArticleContent("Sample Article Content");
    result.setStatus(ArticleStatusEnum.ACTIVE.getStatus());
    result.setCreatedUserId(createdUserId);
    return result;
  }
}
