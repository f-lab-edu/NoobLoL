package com.nooblol.board.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.board.dto.ArticleDto;
import com.nooblol.board.mapper.ArticleMapper;
import com.nooblol.board.utils.ArticleAuthMessage;
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

  @Test
  @DisplayName("일반 사용자이며 게시물을 등록하는 경우에 DB에 정상적으로 데이터가 삽입되면 결과로 True를 Return받는다")
  void upsertArticle_WhenUserIsAuthUserAndInsertIsSuccessThenReturnTrue() {
    //given
    UserDto sessionUserData = new UserDto().builder()
        .userId("test")
        .userEmail("test@test.com")
        .userName("test")
        .userRole(UserRoleStatus.AUTH_USER.getRoleValue())
        .level(1)
        .exp(0)
        .build();

    HttpSession session = new MockHttpSession();
    session.setAttribute(SessionEnum.USER_LOGIN.getValue(), sessionUserData);

    ArticleDto mockArticleDto = new ArticleDto();

    //mock
    when(articleMapper.upsertArticle(mockArticleDto)).thenReturn(1);

    //when
    boolean result = articleService.upsertArticle(mockArticleDto, session, true);

    //then
    assertEquals(result, true);
  }

  @Test
  @DisplayName("사용자 관리자이며 게시물을 등록이나 수정을 하는 경우에 DB에 정상적으로 데이터가 삽입되면 결과로 True를 Return받는다")
  void upsertArticle_WhenUserIsAdminAndUpsertIsSuccessThenReturnTrue() {
    //given
    UserDto sessionUserData = new UserDto().builder()
        .userId("test")
        .userEmail("test@test.com")
        .userName("test")
        .userRole(UserRoleStatus.ADMIN.getRoleValue())
        .level(1)
        .exp(0)
        .build();

    HttpSession session = new MockHttpSession();
    session.setAttribute(SessionEnum.USER_LOGIN.getValue(), sessionUserData);

    ArticleDto mockArticleDto = new ArticleDto();

    //mock
    when(articleMapper.upsertArticle(mockArticleDto)).thenReturn(1);

    //when
    boolean result = articleService.upsertArticle(mockArticleDto, session, false);

    //then
    assertEquals(result, true);
  }

  @Test
  @DisplayName("일반 사용자이며 게시물을 수정하는 경우에 기존 글과 동일한 사용자이면, Return값으로 True를 받는다")
  void upsertArticle_WhenUserIsAuthUserAndUpdateIsSuccessThenReturnTrue() {
    //given
    UserDto sessionUserData = new UserDto().builder()
        .userId("test")
        .userEmail("test@test.com")
        .userName("test")
        .userRole(UserRoleStatus.AUTH_USER.getRoleValue())
        .level(1)
        .exp(0)
        .build();

    HttpSession session = new MockHttpSession();
    session.setAttribute(SessionEnum.USER_LOGIN.getValue(), sessionUserData);

    ArticleDto mockArticleDto = new ArticleDto().builder().articleId(1).build();

    //mock
    when(articleMapper.upsertArticle(mockArticleDto)).thenReturn(1);
    when(articleMapper.selectCreatedUserId(mockArticleDto.getArticleId()))
        .thenReturn(sessionUserData.getUserId());

    //when
    boolean result = articleService.upsertArticle(mockArticleDto, session, false);

    //then
    assertEquals(result, true);
  }

  @Test
  @DisplayName("일반 사용자이며 게시물을 수정하는 경우에 기존 글과 다른 사용자이면, Forbidden Exception이 발생한다")
  void upsertArticle_WhenUserIsAuthUserAndNotEqualCreatedUserThenForbiddenException() {
    //given
    UserDto sessionUserData = new UserDto().builder()
        .userId("test")
        .userEmail("test@test.com")
        .userName("test")
        .userRole(UserRoleStatus.AUTH_USER.getRoleValue())
        .level(1)
        .exp(0)
        .build();

    HttpSession session = new MockHttpSession();
    session.setAttribute(SessionEnum.USER_LOGIN.getValue(), sessionUserData);

    ArticleDto mockArticleDto = new ArticleDto().builder().articleId(1).build();

    //mock
    when(articleMapper.selectCreatedUserId(mockArticleDto.getArticleId()))
        .thenReturn("different User");

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      articleService.upsertArticle(mockArticleDto, session, false);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.FORBIDDEN);
  }

  @Test
  @DisplayName("게시글을 삭제할 때 DB에 데이터가 없는 경우 NoData Exception이 발생한다.")
  void deleteArticle_WhenIsNotHaveArticleInDbThenNoDataException() {
    //given
    int testArticleId = 1;

    //mock
    when(articleMapper.selectArticleByArticleId(testArticleId)).thenReturn(null);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      articleService.deleteArticle(testArticleId, null);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.NO_DATA);
  }

  @Test
  @DisplayName("게시글을 삭제할 때 요청자가 관리자인 경우, 정상삭제 되었으면 Return으로 True값을 받는다.")
  void deleteArticle_WhenUserIsAdminAndDeleteSuccessThenReturnTrue() {
    //given
    int testArticleId = 1;

    UserDto sessionUserData = new UserDto().builder()
        .userId("test")
        .userEmail("test@test.com")
        .userName("test")
        .userRole(UserRoleStatus.ADMIN.getRoleValue())
        .level(1)
        .exp(0)
        .build();
    HttpSession session = new MockHttpSession();
    session.setAttribute(SessionEnum.USER_LOGIN.getValue(), sessionUserData);

    ArticleDto mockReturnDto = new ArticleDto().builder()
        .createdUserId(sessionUserData.getUserId())
        .build();

    //mock
    when(articleMapper.deleteArticleByArticleId(testArticleId)).thenReturn(1);
    when(articleMapper.selectArticleByArticleId(testArticleId)).thenReturn(mockReturnDto);

    //when
    boolean result = articleService.deleteArticle(testArticleId, session);

    //then
    assertEquals(result, true);
  }

  @Test
  @DisplayName("게시글을 삭제할 때 요청자가 작성자인 경우, 정상삭제 되었으면 Return으로 True값을 받는다.")
  void deleteArticle_WhenUserIsAuthUserAndDeleteSuccessThenReturnTrue() {
    //given
    int testArticleId = 1;

    UserDto sessionUserData = new UserDto().builder()
        .userId("test")
        .userEmail("test@test.com")
        .userName("test")
        .userRole(UserRoleStatus.AUTH_USER.getRoleValue())
        .level(1)
        .exp(0)
        .build();
    HttpSession session = new MockHttpSession();
    session.setAttribute(SessionEnum.USER_LOGIN.getValue(), sessionUserData);

    ArticleDto mockReturnDto = new ArticleDto().builder()
        .createdUserId(sessionUserData.getUserId())
        .build();

    //mock
    when(articleMapper.deleteArticleByArticleId(testArticleId)).thenReturn(1);
    when(articleMapper.selectArticleByArticleId(testArticleId)).thenReturn(mockReturnDto);

    //when
    boolean result = articleService.deleteArticle(testArticleId, session);

    //then
    assertEquals(result, true);
  }

  @Test
  @DisplayName("게시글을 삭제할 때 일반사용자가 요청시 작성자가 아닌 경우, ForBidden Exception이 발생한다.")
  void deleteArticle_WhenUserIsNotCreatedUserThenForbiddenException() {
    //given
    int testArticleId = 1;

    UserDto sessionUserData = new UserDto().builder()
        .userId("test")
        .userEmail("test@test.com")
        .userName("test")
        .userRole(UserRoleStatus.AUTH_USER.getRoleValue())
        .level(1)
        .exp(0)
        .build();
    HttpSession session = new MockHttpSession();
    session.setAttribute(SessionEnum.USER_LOGIN.getValue(), sessionUserData);

    ArticleDto mockReturnDto = new ArticleDto().builder()
        .createdUserId("different UserId")
        .build();

    //mock
    when(articleMapper.selectArticleByArticleId(testArticleId)).thenReturn(mockReturnDto);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      articleService.deleteArticle(testArticleId, session);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.FORBIDDEN);
  }
}