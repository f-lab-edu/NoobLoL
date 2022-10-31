package com.nooblol.board.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nooblol.board.dto.ReplyRequestDto.ReplyInsertDto;
import com.nooblol.board.dto.ReplyRequestDto.ReplyUpdateDto;
import com.nooblol.board.service.ArticleReplyService;
import com.nooblol.board.utils.BoardStatusEnum;
import com.nooblol.global.utils.RestDocConfiguration;
import com.nooblol.global.utils.SessionSampleObject;
import java.time.LocalDateTime;
import javax.servlet.http.HttpSession;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ArticleReplyController.class)
@Import(RestDocConfiguration.class)
@AutoConfigureRestDocs
class ArticleReplyControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  ArticleReplyService articleReplyService;


  @Test
  @DisplayName("댓글을 추가 할 때, 등록에 성공하면 200상태코드와 함께 결과값으로 True를 획득한다.")
  void addReply_WhenInsertSuccess_ThenReturnOkAndTrue() throws Exception {
    //given
    int articleId = 1;
    ReplyInsertDto requestDto = new ReplyInsertDto().builder()
        .articleId(articleId)
        .replyContent("Sample Reply Content")
        .status(BoardStatusEnum.ACTIVE.getStatus())
        .build();
    MockHttpSession session = (MockHttpSession) SessionSampleObject.authUserLoginSession;

    //mock
    when(articleReplyService.insertReply(any(ReplyInsertDto.class), any(HttpSession.class)))
        .thenReturn(true);

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders.post("/article/reply/add")
                .content(objectMapper.writeValueAsBytes(requestDto))
                .session(session).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(document(
                "article/reply/add",
                requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE)
                    .description(MediaType.APPLICATION_JSON_VALUE)),
                requestFields(
                    fieldWithPath("articleId").type(int.class).description("댓글을 달고자 하는 게시물 ID"),
                    fieldWithPath("replyContent").type(String.class).description("댓글 내용"),
                    fieldWithPath("status").type(int.class).description("댓글의 상태 값"),
                    fieldWithPath("sortNo").type(int.class).ignored(),
                    fieldWithPath("createdAt").type(LocalDateTime.class).ignored()
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE)
                        .description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("resultCode").type(int.class).description("실행 결과의 상태값"),
                    fieldWithPath("result").type(boolean.class).description("실행 성공 유무")
                )
            )
        );
  }

  @Test
  @DisplayName("댓글을 수정 할 때, 수정이 성공하면 OK상태값과 함께 결과값으로 true를 획득한다.")
  void updateReply_WhenUpdateSuccess_ThenReturnOkAndTrue() throws Exception {
    //given
    int replyId = 1;
    int articleId = 1;
    ReplyUpdateDto requestDto = new ReplyUpdateDto().builder()
        .replyId(replyId)
        .articleId(articleId)
        .replyContent("Sample Update Reply Content")
        .status(BoardStatusEnum.ACTIVE.getStatus())
        .sortNo(1)
        .build();

    MockHttpSession session = (MockHttpSession) SessionSampleObject.authUserLoginSession;

    //mock
    when(articleReplyService.updateReply(any(ReplyUpdateDto.class),
        any(HttpSession.class))).thenReturn(true);

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders.post("/article/reply/update")
                .content(objectMapper.writeValueAsBytes(requestDto))
                .session(session).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document("article/reply/update",
                requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE)
                    .description(MediaType.APPLICATION_JSON_VALUE)),
                requestFields(
                    fieldWithPath("replyId").type(int.class).description("수정하고자 하는 댓글ID"),
                    fieldWithPath("articleId").type(int.class).description("댓글을 달고자 하는 게시물 ID"),
                    fieldWithPath("replyContent").type(String.class).description("댓글 내용"),
                    fieldWithPath("status").type(int.class).description("댓글의 상태 값"),
                    fieldWithPath("sortNo").type(int.class).ignored()
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(
                        MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("resultCode").type(int.class).description("실행 결과의 상태값"),
                    fieldWithPath("result").type(boolean.class).description("실행 성공 유무")
                )
            )
        );
  }


  @Test
  @DisplayName("댓글을 삭제 할 때, 삭제가 성공하면 200상태코드와 함께 결과값으로 True를 획득한다.")
  void deleteReply_WhenDeleteSuccess_ThenReturnOkAndTrue() throws Exception {
    //given
    int replyId = 1;

    MockHttpSession session = (MockHttpSession) SessionSampleObject.authUserLoginSession;

    //mock
    when(articleReplyService.deleteReplyByReplyId(replyId, session))
        .thenReturn(true);

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/article/reply/delete/{replyId}", replyId)
                .session(session)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(document(
                "article/reply/delete",
                pathParameters(
                    parameterWithName("replyId").description("댓글 ID")
                ), responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(
                        MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("resultCode").type(int.class).description("실행 결과의 상태값"),
                    fieldWithPath("result").type(boolean.class).description("실행 성공 유무")
                )
            )
        );
  }
}