package com.nooblol.board.service.impl;

import com.nooblol.board.dto.ArticleDto;
import com.nooblol.board.service.ArticleService;
import com.nooblol.board.mapper.ArticleMapper;
import com.nooblol.board.utils.ArticleAuthMessage;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.user.utils.UserRoleStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final ArticleMapper articleMapper;

  @Override
  public ArticleDto getArticleInfo(int articleId, String userId) {
    articleMapper.addReadCount(articleId);
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
}
