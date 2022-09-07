package com.nooblol.board.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.board.dto.ArticleDto;
import com.nooblol.board.mapper.ArticleMapper;
import com.nooblol.board.utils.ArticleAuthMessage;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.user.utils.UserRoleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

  @Mock
  private ArticleMapper articleMapper;

  @InjectMocks
  private ArticleServiceImpl articleService;

  @Test
  @DisplayName("UserId가 공백인 경우, 결과값이 GUEST로 반환된다")
  void getUserArticleAuth_WhenUserIdIsBlankThenReturnGuest() {
    //given

    //when
    String result = articleService.getUserArticleAuth("");

    //then
    assertEquals(result, ArticleAuthMessage.GUEST.name());
  }

  @Test
  @DisplayName("UserId가 관리자인 경우, 결과값이 ADMIN으로 반환된다.")
  void getUserArticleAuth_WhenUserIdAdminThenReturnAdmin() {
    //given
    String adminUserId = "test-admin-user";

    //mock
    when(articleMapper.selectUserAuth(adminUserId)).thenReturn(UserRoleStatus.ADMIN.getRoleValue());

    //when
    String result = articleService.getUserArticleAuth(adminUserId);

    //then
    assertEquals(result, ArticleAuthMessage.ADMIN.name());
  }

  @Test
  @DisplayName("UserId가 일반 사용자 경우, 결과값이 USER로 반환된다.")
  void getUserArticleAuth_WhenUserIdUserThenReturnUser() {
    //given
    String userId = "test-user";

    //mock
    when(articleMapper.selectUserAuth(userId)).thenReturn(UserRoleStatus.AUTH_USER.getRoleValue());

    //when
    String result = articleService.getUserArticleAuth(userId);

    //then
    assertEquals(result, ArticleAuthMessage.USER.name());
  }

  @Test
  @DisplayName("articleId가 실제로 존재하지 않는 경우 Exception이 발생한다")
  void getArticleInfo_WhenArticleIsNullThenNoDataException() {
    //given
    int emptyArticleId = 99999;

    //mock
    when(articleMapper.selectArticleByArticleId(emptyArticleId)).thenReturn(null);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      articleService.getArticleInfo(emptyArticleId, "");
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.NO_DATA);
  }

  @Test
  @DisplayName("articleId가 실제로 존재하는 경우, 게시물 정보를 반환한다.")
  void getArticleInfo_WhenArticleIdHaveDataThenReturnArticleDto() {
    //given
    int articleId = 1;

    ArticleDto mockData = new ArticleDto().builder()
        .articleId(articleId)
        .bbsId(1)
        .articleTitle("Test Title")
        .status(1)
        .build();

    //mock
    when(articleMapper.selectArticleByArticleId(articleId)).thenReturn(mockData);

    //when
    ArticleDto result = articleService.getArticleInfo(articleId, "");

    //then
    assertEquals(result, mockData);
  }
}