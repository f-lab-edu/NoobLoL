package com.nooblol.board.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.board.dto.ReplyDto;
import com.nooblol.board.dto.ReplyRequestDto.ReplyInsertDto;
import com.nooblol.board.dto.ReplyRequestDto.ReplyUpdateDto;
import com.nooblol.board.mapper.ArticleReplyMapper;
import com.nooblol.board.service.ArticleService;
import com.nooblol.board.utils.BoardStatusEnum;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionEnum;
import com.nooblol.global.utils.SessionUtils;
import com.nooblol.user.dto.UserDto;
import com.nooblol.user.utils.UserRoleStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.util.ObjectUtils;


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
    authUserSession = new MockHttpSession();
    authUserSession.setAttribute(
        SessionEnum.USER_LOGIN.getValue(),
        new UserDto().builder()
            .userId("test")
            .userEmail("test@test.com")
            .userName("test")
            .userRole(UserRoleStatus.AUTH_USER.getRoleValue())
            .level(1)
            .exp(0)
            .build())
    ;

    adminSession = new MockHttpSession();
    adminSession.setAttribute(
        SessionEnum.USER_LOGIN.getValue(),
        new UserDto().builder()
            .userId("admin")
            .userEmail("admin@test.com")
            .userName("admin")
            .userRole(UserRoleStatus.ADMIN.getRoleValue())
            .level(1)
            .exp(0)
            .build());
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

  @Order(1)
  @Nested
  @DisplayName("댓글 조회 테스트")
  class ArticleReplySelectTest {

    @Nested
    @DisplayName("단건 조회")
    class SelectOne {

      @Test
      @DisplayName("단건을 조회할 경우, 댓글이 존재하지 않으면 Null을 획득한다")
      void selectReplyByReplyId_WhenIsNotExistsReplyByDb_ThenReturnNull() {
        //given
        int nullReplyId = 99999;

        //mock
        when(articleReplyMapper.selectReplyByReplyId(nullReplyId)).thenReturn(null);

        //when
        ReplyDto result = articleReplyService.selectReplyByReplyId(nullReplyId);

        //then
        assertTrue(ObjectUtils.isEmpty(result));
      }

      @Test
      @DisplayName("정상적으로 조회가 된 경우, 댓글의 정보를 획득한다")
      void selectReplyByReplyId_WhenIsExistsReplyByDb_ThenReturnReplyDto() {
        //given
        int replyId = 1;

        ReplyDto mockReturnDto = new ReplyDto().builder()
            .replyId(replyId)
            .articleId(1)
            .replyContent("SampleContent")
            .status(BoardStatusEnum.ACTIVE.getStatus())
            .createdUserId("test")
            .createdAt(LocalDateTime.now())
            .build();

        //mock
        when(articleReplyMapper.selectReplyByReplyId(replyId)).thenReturn(mockReturnDto);

        //when
        ReplyDto result = articleReplyService.selectReplyByReplyId(replyId);

        //then
        assertEquals(result, mockReturnDto);

      }
    }

    @Nested
    @DisplayName("게시물의 모든 댓글 리스트 조회")
    class SelectList {

      @Test
      @DisplayName("게시물이 존재하지 않는 경우에는 BadRequest Exception이 발생한다.")
      void selectReplyListByArticleId_WhenIsNotExistsArticle_ThenBadRequestException() {
        //given
        int nullArticleId = 999999;

        //mock
        when(articleService.isNotArticleInDb(nullArticleId)).thenReturn(true);

        //when
        Exception e = assertThrows(IllegalArgumentException.class,
            () -> articleReplyService.selectReplyListByArticleId(nullArticleId));

        //then
        assertTrue(e.getMessage().equals(ExceptionMessage.BAD_REQUEST));
      }

      @Test
      @DisplayName("게시물은 존재하나, 해당 게시물의 댓글이 없는 경우, Null을 획득한다")
      void selectReplyListByArticleId_WhenIsExistsArticleAndIsNotExistsReply_ThenReturnNull() {
        //given
        int existsArticleId = 1;

        //mock
        when(articleService.isNotArticleInDb(existsArticleId)).thenReturn(false);
        when(articleReplyMapper.selectReplyListByArticleId(existsArticleId)).thenReturn(null);

        //when
        List<ReplyDto> result = articleReplyService.selectReplyListByArticleId(existsArticleId);

        //then
        assertTrue(ObjectUtils.isEmpty(result));
      }

      @Test
      @DisplayName("게시물이 존재하고, 댓글이 존재하는 경우 해당 댓글 리스트를 획득한다.")
      void selectReplyListByArticleId_WhenIsExistsArticleAndIsExistsReply_ThenReturnReplyList() {
        //given
        int existsArticleId = 1;

        ArrayList mockReturnList = new ArrayList<ReplyDto>();

        ReplyDto sampleReply = new ReplyDto().builder()
            .replyId(1)
            .articleId(existsArticleId)
            .replyContent("Sample Reply Content")
            .build();

        mockReturnList.add(sampleReply);

        //mock
        when(articleService.isNotArticleInDb(existsArticleId)).thenReturn(false);
        when(articleReplyMapper.selectReplyListByArticleId(existsArticleId)).thenReturn(
            mockReturnList);

        //when
        List result = articleReplyService.selectReplyListByArticleId(existsArticleId);

        //then
        assertEquals(result, mockReturnList);
      }
    }
  }

}