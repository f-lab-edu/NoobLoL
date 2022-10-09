package com.nooblol.board.service.impl;

import com.nooblol.board.dto.ArticleDto;
import com.nooblol.board.dto.ArticleStatusDto;
import com.nooblol.board.mapper.ArticleStatusMapper;
import com.nooblol.board.service.ArticleService;
import com.nooblol.board.mapper.ArticleMapper;
import com.nooblol.board.utils.ArticleAuthMessage;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionUtils;
import com.nooblol.user.utils.UserRoleStatus;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final ArticleMapper articleMapper;

  private final ArticleStatusMapper articleStatusMapper;

  @Override
  public ArticleDto getArticleInfo(int articleId, String userId) {
    addReadCount(articleId);
    ArticleDto result = articleMapper.selectArticleByArticleId(articleId);

    if (ObjectUtils.isEmpty(result)) {
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }

    result.setAuthMessage(getUserArticleAuth(userId));
    return result;
  }

  @Override
  public String getUserArticleAuth(String userId) {
    if (StringUtils.isBlank(userId)) {
      return ArticleAuthMessage.GUEST.name();
    }

    int userAuthData = articleMapper.selectUserAuth(userId);
    if (userAuthData == UserRoleStatus.ADMIN.getRoleValue()) {
      return ArticleAuthMessage.ADMIN.name();
    }

    return ArticleAuthMessage.USER.name();
  }

  @Override
  public void addReadCount(int articleId) {
    articleMapper.addReadCount(articleId);
  }

  @Override
  public boolean insertArticle(ArticleDto articleDto) {
    return articleMapper.insertArticle(articleDto) > 0;
  }

  @Override
  public boolean updateArticle(ArticleDto articleDto, HttpSession session) {
    //관리자 또는 게시글 작성자
    if (isArticleCreatedUserOrAdminUser(
        articleMapper.selectCreatedUserId(articleDto.getArticleId()), session)) {
      return articleMapper.updateArticle(articleDto) > 0;
    }

    //일반 사용자이면서, 게시물의 원작자 여부 확인
    throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public boolean deleteArticle(int articleId, HttpSession session) {
    ArticleDto haveArticleData = articleMapper.selectArticleByArticleId(articleId);

    if (ObjectUtils.isEmpty(haveArticleData)) {
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }

    if (isArticleCreatedUserOrAdminUser(haveArticleData.getCreatedUserId(), session)) {
      return isArticleDeleteSuccess(articleId);
    }
    throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
  }

  @Override
  public void checkNotExistsArticleByArticleId(int articleId) {
    if (ObjectUtils.isEmpty(articleMapper.selectArticleByArticleId(articleId))) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }
  }

  /**
   * 게시물에 대한 작업의 요청자가 관리자나 제작자가 맞는지를 확인한다
   *
   * @param createdUserId 게시물에 대한 작성자 UserId
   * @param session       현재 로그인정보가 들어간 Session
   * @return
   */
  private boolean isArticleCreatedUserOrAdminUser(String createdUserId, HttpSession session) {
    return UserRoleStatus.isUserRoleAdmin(SessionUtils.getSessionUserRole(session)) ||
        (StringUtils.isNotBlank(createdUserId) &&
            createdUserId.equals(SessionUtils.getSessionUserId(session)));
  }


  /**
   * 먼저 추천, 비추천에 대한 기록도 모두 삭제하며,
   * <p>
   * 이후 게시글을 삭제하며, 정상적으로 삭제가 되면 True, 삭제된 건수가 없으면 False를 Return한다
   *
   * @param articleId
   * @return
   */
  private boolean isArticleDeleteSuccess(int articleId) {
    articleStatusMapper.deleteArticleStatus(
        new ArticleStatusDto().builder().articleId(articleId).build()
    );

    return articleMapper.deleteArticleByArticleId(articleId) > 0;
  }
}
