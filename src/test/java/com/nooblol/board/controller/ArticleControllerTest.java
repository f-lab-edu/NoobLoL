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
import com.nooblol.board.dto.LikeAndNotLikeResponseDto;
import com.nooblol.board.service.ArticleService;
import com.nooblol.board.utils.ArticleFixtureUtils;
import com.nooblol.global.utils.ApiDocumentUtils;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.RestDocConfiguration;
import com.nooblol.global.utils.SessionSampleObject;
import com.nooblol.global.utils.SessionUtils;
import java.time.LocalDateTime;
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
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
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
  @DisplayName("게시물 테스트 케이스")
  class ArticleTest {

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
            .andExpect(
                jsonPath("$.resultCode", Is.is(ResponseEnum.OK.getResponse().getResultCode())))
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
                        fieldWithPath("result").type(boolean.class)
                            .description("성공 유무")
                    )
                )
            );
      }
    }

    @Nested
    @DisplayName("게시물 조회")
    class GetArticle {

      private RestDocumentationResultHandler getArticleDocument(String docsPathValue) {
        return document(docsPathValue,
            pathParameters(
                parameterWithName("articleId").description("존재하는 Article(게시물) ID")
            ), responseHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                    MediaType.APPLICATION_JSON_VALUE)
            ), responseFields(
                fieldWithPath("resultCode").type(int.class).description("실행 결과의 상태값"),
                fieldWithPath("result").type(ArticleDto.class).description("게시물 정보"),
                fieldWithPath("result.articleId").type(int.class)
                    .description("Article(게시물) ID"),
                fieldWithPath("result.bbsId").type(int.class).description("게시판 ID"),
                fieldWithPath("result.articleTitle").type(String.class)
                    .description("Article(게시물) 제목"),
                fieldWithPath("result.articleContent").type(String.class)
                    .description("Article(게시물) 내용"),
                fieldWithPath("result.articleReadCount").type(int.class).description("조회수"),
                fieldWithPath("result.status").type(int.class)
                    .description("Article(게시물) 상태"),
                fieldWithPath("result.createdUserId").type(String.class)
                    .description("생성한 사용자 ID"),
                fieldWithPath("result.createdAt").type(LocalDateTime.class)
                    .description("생성일"),
                fieldWithPath("result.updatedAt").type(LocalDateTime.class)
                    .description("최종 수정일"),
                fieldWithPath("result.authMessage").type(String.class)
                    .description("해당글을 조회시 요청할 수 있는 권한")
            )
        );
      }

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
            .andDo(getArticleDocument("article/getArticle/guest"));
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
            .andDo(getArticleDocument("article/getArticle/user"));
      }

      @Test
      @DisplayName("관리자가 실제 존재하는 게시물을 조회하는 경우, 게시물의 정보를 반환한다")
      void getArticle_WhenUserIsAdmiThenReturnArticle() throws Exception {
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
            .andDo(getArticleDocument("article/getArticle/admin"));
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
            .andExpect(
                jsonPath("$.resultCode", Is.is(ResponseEnum.OK.getResponse().getResultCode())))
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

  @Nested
  @DisplayName("게시물 추천, 비추천 테스트 케이스")
  class ArticleStatusTest {

    @Test
    @DisplayName("게시물의 추천과 비추천의 갯수를 조회할 때, articleId가 존재하는 경우 각각 두개의 상태에 대한 갯수를 획득한다.")
    void getLikeAndNotLikeArticle_WhenIsExistsArticleId_ThenReturnLikeAndNotLikeResponseDto()
        throws Exception {
      //given
      int articleId = 1;
      LikeAndNotLikeResponseDto likeAndNotLikeResponseDto = new LikeAndNotLikeResponseDto().builder()
          .likeCnt(5)
          .notLikeCnt(4)
          .build();

      //mock
      when(articleService.likeAndNotListStatus(articleId)).thenReturn(likeAndNotLikeResponseDto);

      //when & then
      mockMvc.perform(
              RestDocumentationRequestBuilders.get("/article/status/{articleId}", articleId)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
          .andDo(document("article/status/getLikeAndNotLike",
                  pathParameters(
                      parameterWithName("articleId").description("존재하는 Article(게시물) ID")
                  ),
                  responseHeaders(
                      headerWithName(HttpHeaders.CONTENT_TYPE)
                          .description(MediaType.APPLICATION_JSON_VALUE)
                  ),
                  responseFields(
                      fieldWithPath("resultCode").type(int.class).description("실행 결과의 상태값"),
                      fieldWithPath("result.likeCnt").type(int.class).description("해당 Article의 추천 수"),
                      fieldWithPath("result.notLikeCnt").type(int.class)
                          .description("해당 Article의 비추천 수")
                  )
              )
          );
    }

    @Test
    @DisplayName("게시물에 대한 추천을 할 경우, 이전에 추천 또는 비추천에 대한 기록이 없는 경우, Ok상태값과 true를 결과값으로 획득한다")
    void likeArticle_WhenIsNotExistsLikeOrNotLikeHistory_ThenReturnOkAndTrue() throws Exception {
      //given
      int articleId = 1;
      MockHttpSession session = (MockHttpSession) SessionSampleObject.authUserLoginSession;

      //mock
      when(articleService.likeArticle(articleId, (HttpSession) session)).thenReturn(true);

      //when & then
      mockMvc.perform(
              RestDocumentationRequestBuilders.get(
                  "/article/like/{articleId}", articleId).session(session)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
          .andExpect(jsonPath("$.result", Is.is(true)))
          .andDo(getStatusDocument("article/status/like")
          );
    }

    @Test
    @DisplayName("게시물에 대한 비추천을 할 경우, 이전에 추천 또는 비추천에 대한 기록이 없는 경우, Ok상태값과 true를 결과값으로 획득한다")
    void NotLikeArticle_WhenIsNotExistsLikeOrNotLikeHistory_ThenReturnOkAndTrue() throws Exception {
      //given
      int articleId = 1;
      MockHttpSession session = (MockHttpSession) SessionSampleObject.authUserLoginSession;

      //mock
      when(articleService.notLikeArticle(articleId, (HttpSession) session)).thenReturn(true);

      //when & then
      mockMvc.perform(
              RestDocumentationRequestBuilders.get(
                  "/article/notLike/{articleId}", articleId).session(session)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
          .andExpect(jsonPath("$.result", Is.is(true)))
          .andDo(getStatusDocument("article/status/notLike")
          );
    }

    private RestDocumentationResultHandler getStatusDocument(String docsPathValue) {
      return document(
          docsPathValue,
          pathParameters(
              parameterWithName("articleId").description("존재하는 Article(게시물) ID")
          ), responseHeaders(
              headerWithName(HttpHeaders.CONTENT_TYPE)
                  .description(MediaType.APPLICATION_JSON_VALUE)
          ),
          responseFields(
              fieldWithPath("resultCode").type(int.class).description("실행 결과의 상태값"),
              fieldWithPath("result").type(boolean.class).description("실행 성공 유무")
          )
      );
    }
  }

}