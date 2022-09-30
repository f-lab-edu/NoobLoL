package com.nooblol.board.service.impl;


import com.nooblol.board.dto.ArticleStatusDto;
import com.nooblol.board.dto.LikeAndNotLikeResponseDto;
import com.nooblol.board.mapper.ArticleMapper;
import com.nooblol.board.mapper.ArticleStatusMapper;
import com.nooblol.board.service.ArticleService;
import com.nooblol.board.service.ArticleStatusService;
import com.nooblol.board.utils.ArticleLikeStatusEnum;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionUtils;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleStatusServiceImpl implements ArticleStatusService {

  private final ArticleStatusMapper articleStatusMapper;

  private final ArticleService articleService;

  @Override
  public boolean likeArticle(int articleId, HttpSession session) {
    validatedNotHaveArticle(articleId);

    ArticleStatusDto requestArticleStatusDto =
        createArticleStatusDto(
            articleId, SessionUtils.getSessionUserId(session), true
        );

    return statusProcess(requestArticleStatusDto);
  }

  @Override
  public boolean notLikeArticle(int articleId, HttpSession session) {
    validatedNotHaveArticle(articleId);

    ArticleStatusDto requestArticleStatusDto =
        createArticleStatusDto(
            articleId, SessionUtils.getSessionUserId(session), false
        );

    return statusProcess(requestArticleStatusDto);
  }

  @Override
  public LikeAndNotLikeResponseDto likeAndNotListStatus(int articleId) {
    return articleStatusMapper.selectArticleAllStatusByArticleId(articleId);
  }

  private void validatedNotHaveArticle(int articleId) {
    if (articleService.isNotArticleInDb(articleId)) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }
  }

  private ArticleStatusDto createArticleStatusDto(int articleId, String userId, boolean type) {
    ArticleStatusDto articleStatusDto = new ArticleStatusDto().builder()
        .articleId(articleId)
        .userId(userId)
        .likeType(ArticleLikeStatusEnum.findLikeStatusType(type))
        .build();

    return articleStatusDto;
  }

  /**
   * 추천, 비추천에 대한 프로세스, 해당 게시물에 대해 사용자가 좋아요가 없는 경우 Insert 이미 같은 타입(추천, 비추천)을 한경우는 삭제, 다른 타입인 경우는
   * Exception이 발생한다
   *
   * @param requestArticleStatusDto
   * @return
   */
  private boolean statusProcess(ArticleStatusDto requestArticleStatusDto) {
    ArticleStatusDto IsHaveStatusData = articleStatusMapper.selectArticleStatusByArticleIdAndUserId(
        requestArticleStatusDto);

    if (ObjectUtils.isEmpty(IsHaveStatusData)) {
      return articleStatusMapper.insertArticleStatus(requestArticleStatusDto) > 0;
    }

    if (IsHaveStatusData.getLikeType().isLikeStatus() !=
        requestArticleStatusDto.getLikeType().isLikeStatus()) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }

    return articleStatusMapper.deleteArticleStatus(requestArticleStatusDto) > 0;
  }
}
