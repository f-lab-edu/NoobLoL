package com.nooblol.board.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.board.dto.ReplyRequestDto.ReplyInsertDto;
import com.nooblol.board.dto.ReplyRequestDto.ReplyUpdateDto;
import com.nooblol.board.mapper.ArticleReplyMapper;
import com.nooblol.board.service.ArticleService;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionSampleObject;
import com.nooblol.global.utils.SessionUtils;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ArticleReplyServiceImplTest {

  @Mock
  private ArticleReplyMapper articleReplyMapper;

  @Mock
  private ArticleService articleService;

  @InjectMocks
  private ArticleReplyServiceImpl articleReplyService;


  private HttpSession authUserSession;
  private HttpSession adminSession;

  @BeforeEach
  void setUp() {
    authUserSession = SessionSampleObject.authUserLoginSession;

    adminSession = SessionSampleObject.adminUserLoginSession;
  }


  @Test
  @DisplayName("댓글을 추가할 때, 게시물이 실제로 존재하지 않으면 BadRequestException이 발생한다")
  void insertReply_WhenDbNotHaveArticleThenBadRequestException() {
    //given
    int testArticleId = 99999;

    ReplyInsertDto replyInsertDto = new ReplyInsertDto().builder()
        .articleId(testArticleId)
        .build();
    //mock
    when(articleService.isNotArticleInDb(testArticleId)).thenReturn(true);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      articleReplyService.insertReply(replyInsertDto, authUserSession);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.BAD_REQUEST);
  }

  @Test
  @DisplayName("댓글을 추가가 정상적으로 이뤄진 경우 결과값으로 True를 획득한다")
  void insertReply_WhenInsertSuccessThenBadReReturnTrue() {
    //given
    int testArticleId = 1;

    ReplyInsertDto replyInsertDto = new ReplyInsertDto().builder()
        .articleId(testArticleId)
        .status(1)
        .build();

    //mock
    when(articleService.isNotArticleInDb(testArticleId)).thenReturn(false);
    when(articleReplyMapper.upsertReply(any())).thenReturn(1);
    when(articleReplyMapper.selectMaxReplyId()).thenReturn(1);
    when(articleReplyMapper.selectMaxSortNoByArticleId(replyInsertDto.getArticleId()))
        .thenReturn(1);

    //when
    boolean result = articleReplyService.insertReply(replyInsertDto, authUserSession);

    //then
    assertEquals(result, true);
  }

  @Test
  @DisplayName("댓글을 수정 할 때, 게시물이 실제로 존재하지 않으면 BadRequestException이 발생한다")
  void updateReply_WhenDbNotHaveArticleThenBadRequestException() {
    //given
    int testArticleId = 99999;
    int testReplyId = 1;

    ReplyUpdateDto replyUpdateDto = new ReplyUpdateDto().builder()
        .articleId(testArticleId)
        .replyId(testReplyId)
        .build();
    //mock
    when(articleService.isNotArticleInDb(testArticleId)).thenReturn(true);

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      articleReplyService.updateReply(replyUpdateDto, authUserSession);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.BAD_REQUEST);
  }

  @Test
  @DisplayName("댓글을 수정 할 때, 수정자가 일반 사용자인데, 작성자가 아니면 ForbiddenException이 발생한다")
  void updateReply_WhenIsNotCreatedUserAndAuthUserThenForbiddenException() {
    //given
    int testArticleId = 99999;
    int testReplyId = 1;

    ReplyUpdateDto replyUpdateDto = new ReplyUpdateDto().builder()
        .articleId(testArticleId)
        .replyId(testReplyId)
        .build();

    //mock
    when(articleService.isNotArticleInDb(testArticleId)).thenReturn(false);
    when(articleReplyMapper.selectCreatedUserIdByReplyId(testReplyId)).thenReturn("NoUserId");

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      articleReplyService.updateReply(replyUpdateDto, authUserSession);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.FORBIDDEN);
  }

  @Test
  @DisplayName("댓글을 수정 할 때, 수정자가 관리자이고, 정상적으로 Update가 되면 결과값으로 True를 획득한다")
  void updateReply_WhenIsNotCreatedUserAndAdminAndSuccessUpdateThenReturnTrue() {
    //given
    int testArticleId = 99999;
    int testReplyId = 1;

    ReplyUpdateDto replyUpdateDto = new ReplyUpdateDto().builder()
        .articleId(testArticleId)
        .replyId(testReplyId)
        .status(1)
        .build();

    //mock
    when(articleService.isNotArticleInDb(testArticleId)).thenReturn(false);
    when(articleReplyMapper.selectCreatedUserIdByReplyId(testReplyId)).thenReturn("NoUserId");
    when(articleReplyMapper.upsertReply(any())).thenReturn(1);

    //when
    boolean result = articleReplyService.updateReply(replyUpdateDto, adminSession);

    //then
    assertEquals(result, true);
  }

  @Test
  @DisplayName("댓글을 수정 할 때, 수정자가 관리자이고, Update가 이뤄진 데이터가 없으면 결과값으로 false를 획득한다")
  void updateReply_WhenIsNotCreatedUserAndAdminAndNotUpdateDataThenReturnFalse() {
    //given
    int testArticleId = 99999;
    int testReplyId = 1;

    ReplyUpdateDto replyUpdateDto = new ReplyUpdateDto().builder()
        .articleId(testArticleId)
        .replyId(testReplyId)
        .status(1)
        .build();

    //mock
    when(articleService.isNotArticleInDb(testArticleId)).thenReturn(false);
    when(articleReplyMapper.selectCreatedUserIdByReplyId(testReplyId)).thenReturn("NoUserId");
    when(articleReplyMapper.upsertReply(any())).thenReturn(0);

    //when
    boolean result = articleReplyService.updateReply(replyUpdateDto, adminSession);

    //then
    assertEquals(result, false);
  }

  @Test
  @DisplayName("댓글을 수정 할 때, 수정자가 본인이며 정상적으로 Update가 되면 결과값으로 True를 획득한다")
  void updateReply_WhenIsCreatedUserAndSuccessUpdateThenReturnTrue() {
    //given
    int testArticleId = 99999;
    int testReplyId = 1;

    ReplyUpdateDto replyUpdateDto = new ReplyUpdateDto().builder()
        .articleId(testArticleId)
        .replyId(testReplyId)
        .status(1)
        .build();

    //mock
    when(articleService.isNotArticleInDb(testArticleId)).thenReturn(false);
    when(articleReplyMapper.selectCreatedUserIdByReplyId(testReplyId)).thenReturn(
        SessionUtils.getSessionUserId(authUserSession));
    when(articleReplyMapper.upsertReply(any())).thenReturn(1);

    //when
    boolean result = articleReplyService.updateReply(replyUpdateDto, authUserSession);

    //then
    assertEquals(result, true);
  }

  @Test
  @DisplayName("댓글을 수정 할 때, 수정자가 본인이며 Update가 성공한 데이터가 없는 경우 결과값으로 False를 획득한다")
  void updateReply_WhenIsCreatedUserNotUpdateDataThenReturnFalse() {
    //given
    int testArticleId = 99999;
    int testReplyId = 1;

    ReplyUpdateDto replyUpdateDto = new ReplyUpdateDto().builder()
        .articleId(testArticleId)
        .replyId(testReplyId)
        .status(1)
        .build();

    //mock
    when(articleService.isNotArticleInDb(testArticleId)).thenReturn(false);
    when(articleReplyMapper.selectCreatedUserIdByReplyId(testReplyId)).thenReturn(
        SessionUtils.getSessionUserId(authUserSession));
    when(articleReplyMapper.upsertReply(any())).thenReturn(0);

    //when
    boolean result = articleReplyService.updateReply(replyUpdateDto, authUserSession);

    //then
    assertEquals(result, false);
  }

  @Test
  @DisplayName("댓글을 삭제 할 때, 요청자가 일반사용자 이면서 댓글 작성자가 아닌 경우, ForbiddenException이 발생한다")
  void deleteReply_WhenIsNotCreatedUserAndAuthUserThenForbiddenException() {
    //given
    int testReplyId = 99999;

    //mock
    when(articleReplyMapper.selectCreatedUserIdByReplyId(testReplyId))
        .thenReturn("NotCreatedUserId");

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      articleReplyService.deleteReplyByReplyId(testReplyId, authUserSession);
    });

    //then
    assertEquals(e.getMessage(), ExceptionMessage.FORBIDDEN);
  }

  @Test
  @DisplayName("댓글을 삭제 할 때, 요청자가 작성자이고 데이터의 삭제가 이뤄진게 있으면 결과값으로 True를 획득한다.")
  void deleteReply_WhenIsCreatedUserAndDeleteSuccessThenReturnTrue() {
    //given
    int testReplyId = 1;

    //mock
    when(articleReplyMapper.selectCreatedUserIdByReplyId(testReplyId))
        .thenReturn("test");
    when(articleReplyMapper.deleteReplyByReplyId(testReplyId)).thenReturn(1);

    //when
    boolean result = articleReplyService.deleteReplyByReplyId(testReplyId, authUserSession);

    //then
    assertEquals(result, true);
  }

  @Test
  @DisplayName("댓글을 삭제 할 때, 요청자가 작성자이고 데이터의 삭제가 이뤄진게 없으면, 결과값은 False를 획득한다.")
  void deleteReply_WhenIsCreatedUserAndNoHaveDeleteDataThenReturnFalse() {
    //given
    int testReplyId = 1;

    //mock
    when(articleReplyMapper.selectCreatedUserIdByReplyId(testReplyId))
        .thenReturn("test");
    when(articleReplyMapper.deleteReplyByReplyId(testReplyId)).thenReturn(0);

    //when
    boolean result = articleReplyService.deleteReplyByReplyId(testReplyId, authUserSession);

    //then
    assertEquals(result, false);
  }

  @Test
  @DisplayName("댓글을 삭제 할 때, 요청자가 관리자이고 데이터의 삭제가 이뤄진게 있으면 결과값으로 True를 획득한다.")
  void deleteReply_WhenReqUserIsAdminAndDeleteSuccessThenReturnTrue() {
    //given
    int testReplyId = 1;

    //mock
    when(articleReplyMapper.selectCreatedUserIdByReplyId(testReplyId))
        .thenReturn("NotCreatedUserId");
    when(articleReplyMapper.deleteReplyByReplyId(testReplyId)).thenReturn(1);

    //when
    boolean result = articleReplyService.deleteReplyByReplyId(testReplyId, adminSession);

    //then
    assertEquals(result, true);
  }

  @Test
  @DisplayName("댓글을 삭제 할 때, 요청자가 관리자이고 데이터의 삭제가 이뤄진게 없으면, 결과값은 False를 획득한다")
  void deleteReply_WhenReqUserIsAdminAndNoHaveDeleteDataThenReturnFalse() {
    //given
    int testReplyId = 1;

    //mock
    when(articleReplyMapper.selectCreatedUserIdByReplyId(testReplyId))
        .thenReturn("NotCreatedUserId");
    when(articleReplyMapper.deleteReplyByReplyId(testReplyId)).thenReturn(0);

    //when
    boolean result = articleReplyService.deleteReplyByReplyId(testReplyId, adminSession);

    //then
    assertEquals(result, false);
  }
}