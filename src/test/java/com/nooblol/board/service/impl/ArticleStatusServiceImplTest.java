package com.nooblol.board.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.nooblol.board.dto.ArticleStatusDto;
import com.nooblol.board.mapper.ArticleStatusMapper;
import com.nooblol.board.utils.ArticleLikeStatus;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionEnum;
import com.nooblol.user.dto.UserDto;
import com.nooblol.user.utils.UserRoleStatus;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

@ExtendWith(MockitoExtension.class)
class ArticleStatusServiceImplTest {

  @Mock
  private ArticleStatusMapper articleStatusMapper;

  @Mock
  private ArticleServiceImpl articleService;

  @InjectMocks
  private ArticleStatusServiceImpl articleStatusService;


  @Test
  @DisplayName("게시글을 추천 또는 비추천 할 때, DB에 게시물이 존재하지 않으면 BadRequest Exception이 발생한다")
  void likeArticle_WhenHaveNotInDbThenBadRequestException() {
    //given
    int testArticleId = 3;

    //mock
    doThrow(new IllegalArgumentException(ExceptionMessage.BAD_REQUEST))
        .when(articleService).checkNotExistsArticleByArticleId(testArticleId);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      articleStatusService.likeArticle(testArticleId, null);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.BAD_REQUEST);
  }

  @Test
  @DisplayName("게시글을 추천 또는 비추천 하려고 할 때, 추천했던 적이 없으면 결과값으로 true를 획득한다")
  void likeArticle_WhenArticleFirstLikeThenReturnTrue() {
    //given
    int testArticleId = 1;

    UserDto mockUserDto = new UserDto().builder()
        .userId("test")
        .userEmail("test@test.com")
        .userName("test")
        .userRole(UserRoleStatus.AUTH_USER.getRoleValue())
        .level(1)
        .exp(0)
        .build();

    HttpSession session = new MockHttpSession();
    session.setAttribute(SessionEnum.USER_LOGIN.getValue(), mockUserDto);

    //mock
    when(articleStatusMapper.selectArticleStatusByArticleIdAndUserId(any())).thenReturn(null);
    when(articleStatusMapper.insertArticleStatus(any())).thenReturn(1);

    //then
    boolean result = articleStatusService.likeArticle(testArticleId, session);

    assertEquals(result, true);
  }

  @Test
  @DisplayName("게시글을 추천할떄, 이미 비추천을 한상황이면 BadRequest Exception이 발생한다.")
  void likeArticle_WhenArticleLikeAndHistoryIsNotLikeThenBadRequestException() {
    //given
    int testArticleId = 1;

    UserDto mockUserDto = new UserDto().builder()
        .userId("test")
        .userEmail("test@test.com")
        .userName("test")
        .userRole(UserRoleStatus.AUTH_USER.getRoleValue())
        .level(1)
        .exp(0)
        .build();

    HttpSession session = new MockHttpSession();
    session.setAttribute(SessionEnum.USER_LOGIN.getValue(), mockUserDto);

    ArticleStatusDto mockArticleStatusDto = new ArticleStatusDto().builder()
        .articleId(testArticleId)
        .userId(mockUserDto.getUserId())
        .likeType(ArticleLikeStatus.NOT_LIKE)
        .build();

    //mock
    when(articleStatusMapper.selectArticleStatusByArticleIdAndUserId(any())).thenReturn(
        mockArticleStatusDto);

    //when
    Exception result = assertThrows(IllegalArgumentException.class, () -> {
      articleStatusService.likeArticle(testArticleId, session);
    });

    //then
    assertEquals(result.getMessage(), ExceptionMessage.BAD_REQUEST);
  }

  @Test
  @DisplayName("게시글을 추천할떄, 이미 추천을 한상황이면 추천했던 이력을 삭제후 Return값으로 True를 획득한다")
  void likeArticle_WhenArticleLikeAndHistoryIsLikeThenReturnTrue() {
    //given
    int testArticleId = 1;

    UserDto mockUserDto = new UserDto().builder()
        .userId("test")
        .userEmail("test@test.com")
        .userName("test")
        .userRole(UserRoleStatus.AUTH_USER.getRoleValue())
        .level(1)
        .exp(0)
        .build();

    HttpSession session = new MockHttpSession();
    session.setAttribute(SessionEnum.USER_LOGIN.getValue(), mockUserDto);

    ArticleStatusDto mockArticleStatusDto = new ArticleStatusDto().builder()
        .articleId(testArticleId)
        .userId(mockUserDto.getUserId())
        .likeType(ArticleLikeStatus.LIKE)
        .build();

    //mock
    when(articleStatusMapper.selectArticleStatusByArticleIdAndUserId(any())).thenReturn(
        mockArticleStatusDto);
    when(articleStatusMapper.deleteArticleStatus(any())).thenReturn(1);

    //when
    boolean result = articleStatusService.likeArticle(testArticleId, session);

    //then
    assertEquals(result, true);
  }
}