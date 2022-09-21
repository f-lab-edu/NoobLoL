package com.nooblol.board.controller;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nooblol.board.dto.ArticleDto;
import com.nooblol.board.dto.ArticleInsertRequestDto;
import com.nooblol.board.service.ArticleService;
import com.nooblol.board.utils.ArticleFixtureUtils;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.RestDocConfiguration;
import com.nooblol.global.utils.SessionSampleObject;
import com.nooblol.global.utils.SessionUtils;
import javax.servlet.http.HttpSession;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

@WebMvcTest(ArticleController.class)
@Import(RestDocConfiguration.class)
@AutoConfigureRestDocs
class ArticleControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ArticleService articleService;

  @Autowired
  ObjectMapper objectMapper;

  @Nested
  @DisplayName("게시물 생성")
  class AddArticle {

    @Test
    @DisplayName("게시물 생성시 사용자 로그인이 되어 있는 경우에는 정상적으로 데이터가 삽입된다.")
    void insertArticle_WhenLoginSessionIsUserThenReturnIsOk() throws Exception {
      //given
      MockHttpSession session = (MockHttpSession) SessionSampleObject.authUserLoginSession;
      ArticleInsertRequestDto requestDto =
          ArticleFixtureUtils.insertArticleFixture(SessionUtils.getSessionUserId(session));

      //mock
      when(articleService.getNewArticleId()).thenReturn(1);
      when(
          articleService.upsertArticle(any(), any(HttpSession.class), anyBoolean())
      ).thenReturn(true);

      //then & when
      mockMvc.perform(RestDocumentationRequestBuilders.post("/article/insert")
              .content(objectMapper.writeValueAsBytes(requestDto))
              .session(session).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(ResponseEnum.OK.getResponse().getResultCode())))
          .andExpect(jsonPath("$.result", Is.is(true)))
          .andDo(
              document(
                  "article/addArticle",
                  requestHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description(
                      MediaType.APPLICATION_JSON_VALUE)),
                  requestFields(
                      fieldWithPath("bbsId").type(int.class).description("작성된 게시판 ID"),
                      fieldWithPath("articleTitle").type(String.class).description("게시글 제목"),
                      fieldWithPath("articleContent").type(String.class).description("게시글 내용"),
                      fieldWithPath("status").type(int.class).description("게시글 상태값"),
                      fieldWithPath("createdUserId").ignored(),
                      fieldWithPath("articleReadCount").ignored(),
                      fieldWithPath("createdAt").ignored(),
                      fieldWithPath("updatedAt").ignored()
                  ),
                  responseHeaders(
                      headerWithName(HttpHeaders.CONTENT_TYPE).description(
                          MediaType.APPLICATION_JSON_VALUE)
                  ),
                  responseFields(
                      fieldWithPath("resultCode").type(int.class).description("실행 결과의 상태값"),
                      fieldWithPath("result").type(Object.class)
                          .description("성공 유무 또는 오류에 대한 Message")
                  )
              )
          );
    }
  }

  @Nested
  @DisplayName("게시물 조회")
  class GetArticle {

    @Test
    @DisplayName("일반 Guest가 실제 존재하는 게시물을 조회하는 경우, 게시물의 정보를 반환한다.")
    void getArticle_WhenUserIsGuestThenReturnArticle() throws Exception {
      //given
      int articleId = 1;
      MockHttpSession session = new MockHttpSession();
      ArticleDto response = ArticleFixtureUtils.guestActiveArticleFixture(articleId, "test");

      //mock
      when(
          articleService.getArticleInfo(articleId, SessionUtils.getSessionUserId(session))
      ).thenReturn(response);

      //when & then
      mockMvc.perform(
              RestDocumentationRequestBuilders.
                  get("/article/{articleId}", articleId).session(session)
                  .contentType(MediaType.APPLICATION_JSON)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
          .andDo(
              document("article/getArticle/guest",
                  pathParameters(
                      parameterWithName("articleId").description("존재하는 Article(게시물) ID")
                  )
              )
          );
    }

    @Test
    @DisplayName("일반 사용자가 실제 존재하는 게시물을 조회하는 경우, 게시물의 정보를 반환한다")
    void getArticle_WhenUserIsAuthUserThenReturnArticle() throws Exception {
      //given
      int articleId = 1;
      MockHttpSession session = (MockHttpSession) SessionSampleObject.authUserLoginSession;
      ArticleDto response = ArticleFixtureUtils.userActiveArticleFixture(articleId, "test");

      //mock
      when(
          articleService.getArticleInfo(articleId, SessionUtils.getSessionUserId(session))
      ).thenReturn(response);

      //when & then
      mockMvc.perform(
              RestDocumentationRequestBuilders.
                  get("/article/{articleId}", articleId).session(session)
                  .contentType(MediaType.APPLICATION_JSON)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
          .andDo(
              document("article/getArticle/user",
                  pathParameters(
                      parameterWithName("articleId").description("존재하는 Article(게시물) ID")
                  )
              )
          );
    }

    @Test
    @DisplayName("관리자가 실제 존재하는 게시물을 조회하는 경우, 게시물의 정보를 반환한다")
    void getArticle_WhenUserIsAdminThenReturnArticle() throws Exception {
      //given
      int articleId = 1;
      MockHttpSession session = (MockHttpSession) SessionSampleObject.adminUserLoginSession;
      ArticleDto response = ArticleFixtureUtils.adminActiveArticleFixture(articleId, "test");

      //mock
      when(
          articleService.getArticleInfo(articleId, SessionUtils.getSessionUserId(session))
      ).thenReturn(response);

      //when & then
      mockMvc.perform(
              RestDocumentationRequestBuilders.
                  get("/article/{articleId}", articleId).session(session)
                  .contentType(MediaType.APPLICATION_JSON)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
          .andDo(
              document("article/getArticle/admin",
                  pathParameters(
                      parameterWithName("articleId").description("존재하는 Article(게시물) ID")
                  )
              )
          );

    }

  }

  @Nested
  @DisplayName("게시물 삭제")
  class DeleteArticle {

    @Test
    @DisplayName("게시물의 정상 삭제시 결과값으로 Ok와 true를 획득한다")
    void deleteArticle_WhenUserIsAuthUser_ThenReturnOkAndTrue() throws Exception {
      //given
      int articleId = 1;
      MockHttpSession session = (MockHttpSession) SessionSampleObject.authUserLoginSession;

      //mock
      when(articleService.deleteArticle(articleId, session)).thenReturn(true);

      mockMvc.perform(
              RestDocumentationRequestBuilders.delete(
                  "/article/delete/{articleId}", articleId
              ).session(session)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(ResponseEnum.OK.getResponse().getResultCode())))
          .andExpect(jsonPath("$.result", Is.is(true)))
          .andDo(
              document(
                  "article/deleteArticle",
                  pathParameters(
                      parameterWithName("articleId").description("삭제할 Article(게시물) ID")
                  ),
                  responseHeaders(
                      headerWithName(HttpHeaders.CONTENT_TYPE).description(
                          MediaType.APPLICATION_JSON_VALUE)
                  ),
                  responseFields(
                      fieldWithPath("resultCode").type(int.class).description("실행 결과의 상태값"),
                      fieldWithPath("result").type(Object.class)
                          .description("성공 유무 또는 오류에 대한 Message")
                  )
              )
          );
    }
  }

}