package com.nooblol.board.service.impl;

import com.nooblol.board.dto.ArticleDto;
import com.nooblol.board.dto.ArticleStatusDto;
import com.nooblol.board.dto.LikeAndNotLikeResponseDto;
import com.nooblol.board.mapper.ArticleReplyMapper;
import com.nooblol.board.service.ArticleService;
import com.nooblol.board.mapper.ArticleMapper;
import com.nooblol.board.utils.ArticleAuthMessage;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.EncryptUtils;
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

  private final ArticleReplyMapper articleReplyMapper;

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
  public boolean upsertArticle(ArticleDto articleDto, HttpSession session, boolean isInsert) {
    //UserLoginCheck의 Annotation을 통해 무조건 Session로그인이 확인된 상황이기에, Role이 Null이 올 수 없음

    boolean isUserAdminOrInsertArticle =
        UserRoleStatus.isUserRoleAdmin(SessionUtils.getSessionUserRole(session)) || isInsert;

    if (isUserAdminOrInsertArticle) {
      return isArticleUpsertSuccess(articleDto);
    }

    //일반 사용자이면서, 게시물의 원작자 여부 확인
    boolean isNotCreatedUser = EncryptUtils.isNotCreatedUser(
        articleMapper.selectCreatedUserId(articleDto.getArticleId()),
        SessionUtils.getSessionUserId(session)
    );

    if (isNotCreatedUser) {
      throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
    }

    return isArticleUpsertSuccess(articleDto);
  }

  @Override
  public int getNewArticleId() {
    return articleMapper.selectMaxArticleId();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public boolean deleteArticle(int articleId, HttpSession session) {
    ArticleDto haveArticleData = articleMapper.selectArticleByArticleId(articleId);

    if (ObjectUtils.isEmpty(haveArticleData)) {
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }

    boolean isUserAdmin = UserRoleStatus.isUserRoleAdmin(SessionUtils.getSessionUserRole(session));
    if (isUserAdmin) {
      return isArticleDeleteSuccess(articleId);
    }

    boolean isNotCreatedUser = isNotArticleCreatedUser(
        haveArticleData.getCreatedUserId(), SessionUtils.getSessionUserId(session)
    );

    if (isNotCreatedUser) {
      throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
    }

    return isArticleDeleteSuccess(articleId);
  }

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
    return articleMapper.selectArticleAllStatusByArticleId(articleId);
  }

  /**
   * Upsert가 정상적으로 진행된 경우 True를 Return한다.
   *
   * @param articleDto
   * @return
   */
  private boolean isArticleUpsertSuccess(ArticleDto articleDto) {
    return articleMapper.upsertArticle(articleDto) > 0;
  }

  /**
   * 게시글을 작성한 사용자가 Session에 저장된 사용자가 아닌 경우 True를 Return한다.
   *
   * @param dbCreatedUserId 데이터가 없는 경우 빈값이 올 수 있기에 무조건 첫번쨰 파라미터는 DB의 CreatedUserId를 넣어야 한다.
   * @param sessionUserId   Session에 존재하는 로그인된 사용자 Id
   * @return
   */
  private boolean isNotArticleCreatedUser(String dbCreatedUserId, String sessionUserId) {
    return StringUtils.isBlank(dbCreatedUserId) || !dbCreatedUserId.equals(sessionUserId);
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
    articleMapper.deleteArticleStatue(
        new ArticleStatusDto().builder().articleId(articleId).build()
    );

    return articleMapper.deleteArticleByArticleId(articleId) > 0;
  }


  public boolean isNotArticleInDb(int articleId) {
    return ObjectUtils.isEmpty(articleMapper.selectArticleByArticleId(articleId));
  }

  private void validatedNotHaveArticle(int articleId) {
    if (isNotArticleInDb(articleId)) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }
  }

  private ArticleStatusDto createArticleStatusDto(int articleId, String userId, boolean type) {
    ArticleStatusDto articleStatusDto = new ArticleStatusDto().builder()
        .articleId(articleId)
        .userId(userId)
        .type(type)
        .build();

    articleStatusDto.setCreatedAtNow();

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
    ArticleStatusDto IsHaveStatusData = articleMapper.selectArticleStatusByArticleIdAndUserId(
        requestArticleStatusDto);

    if (ObjectUtils.isEmpty(IsHaveStatusData)) {
      return articleMapper.insertArticleStatus(requestArticleStatusDto) > 0;
    }

    if (IsHaveStatusData.isType() != requestArticleStatusDto.isType()) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }

    return articleMapper.deleteArticleStatue(requestArticleStatusDto) > 0;
  }


}
