package com.nooblol.board.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nooblol.board.dto.BbsDto;
import com.nooblol.board.dto.BbsRequestDto.BbsInsertDto;
import com.nooblol.board.dto.BbsRequestDto.BbsUpdateDto;
import com.nooblol.board.dto.CategoryDto;
import com.nooblol.board.dto.CategoryRequestDto.CategoryInsertDto;
import com.nooblol.board.dto.CategoryRequestDto.CategoryUpdateDto;
import com.nooblol.board.service.CategoryService;
import com.nooblol.board.utils.BoardFixtureUtils;
import com.nooblol.board.utils.BoardStatusEnum;
import com.nooblol.global.utils.DocumentSnippetsUtils;
import com.nooblol.global.utils.RestDocConfiguration;
import com.nooblol.global.utils.SessionSampleObject;
import com.nooblol.global.utils.SessionUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(BoardController.class)
@Import(RestDocConfiguration.class)
@AutoConfigureRestDocs
class BoardControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  CategoryService categoryService;


  @Nested
  @DisplayName("카테고리 테스트 케이스")
  class CategoryTest {

    @Test
    @DisplayName("카테고리 리스트 조회요청시, 상태값으로 OK와 카테고리 리스트를 획득한다")
    void getCategoryList_WhenRequest_ThenReturnOkAndCategoryList() throws Exception {
      //given
      int status = BoardStatusEnum.ACTIVE.getStatus();

      CategoryDto categoryDto1 = new CategoryDto().builder().categoryId(1).categoryName("sample1")
          .status(BoardStatusEnum.ACTIVE.getStatus()).createdUserId("test")
          .createdAt(LocalDateTime.now()).updatedUserId("test").updatedAt(LocalDateTime.now())
          .build();

      CategoryDto categoryDto2 = new CategoryDto().builder().categoryId(2).categoryName("sample2")
          .status(BoardStatusEnum.ACTIVE.getStatus()).createdUserId("test")
          .createdAt(LocalDateTime.now()).updatedUserId("test").updatedAt(LocalDateTime.now())
          .build();

      CategoryDto categoryDto3 = new CategoryDto().builder().categoryId(3).categoryName("sample3")
          .status(BoardStatusEnum.ACTIVE.getStatus()).createdUserId("test")
          .createdAt(LocalDateTime.now()).updatedUserId("test").updatedAt(LocalDateTime.now())
          .build();

      List<CategoryDto> returnList = new ArrayList<>();
      returnList.add(categoryDto1);
      returnList.add(categoryDto2);
      returnList.add(categoryDto3);

      //mock
      when(categoryService.getCategoryList(status)).thenReturn(returnList);

      //when & then
      mockMvc.perform(MockMvcRequestBuilders.get("/board/category/list")
              .param("status", String.valueOf(status))).andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value()))).andDo(
              document("board/category/getList", requestParameters(
                      parameterWithName("status").description(
                          "검색을 원하는 카테고리 status, 입력되지 않은 경우 활성화되어있는 카테고리를 기본값으로 가져온다.")),
                  DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                  responseFields(
                      fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("실행 결과의 상태값"),
                      fieldWithPath("result").type(JsonFieldType.ARRAY).description("카테고리 검색조회 결과"),
                      fieldWithPath("result[].categoryId").type(JsonFieldType.NUMBER)
                          .description("카테고리ID"),
                      fieldWithPath("result[].categoryName").type(JsonFieldType.STRING)
                          .description("카테고리 명"),
                      fieldWithPath("result[].status").type(JsonFieldType.NUMBER)
                          .description("해당 카테고리 사용방법 상태"),
                      fieldWithPath("result[].createdUserId").type(JsonFieldType.STRING)
                          .description("생성한 관리자 ID"),
                      fieldWithPath("result[].createdAt").type(JsonFieldType.STRING).description("생성일"),
                      fieldWithPath("result[].updatedUserId").type(JsonFieldType.STRING)
                          .description("최종 수정한 관리자 ID"),
                      fieldWithPath("result[].updatedAt").type(JsonFieldType.STRING)
                          .description("최종 수정일"))));


    }

    @Test
    @DisplayName("카테고리 추가시 카테고리가 추가되었으면, 상태값으로 OK와 결과값으로 true를 획득한다")
    void insertCategory_WhenInsertSuccess_ThenReturnOkAndTrue() throws Exception {
      //given
      MockHttpSession adminSession = (MockHttpSession) SessionSampleObject.adminUserLoginSession;

      CategoryInsertDto requestDto = new CategoryInsertDto().builder()
          .categoryName("Sample Add Category").status(BoardStatusEnum.ACTIVE.getStatus())
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

      //mock
      when(categoryService.insertCategory(any(CategoryInsertDto.class),
          any(HttpSession.class))).thenReturn(true);

      //when & then
      mockMvc.perform(post("/board/category").contentType(MediaType.APPLICATION_JSON_VALUE)
              .content(objectMapper.writeValueAsBytes(requestDto)).session(adminSession))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
          .andExpect(jsonPath("$.result", Is.is(true))).andDo(document("board/category/add",
              DocumentSnippetsUtils.requestHeaders_ContentTypeApplicationJsonValue(), requestFields(
                  fieldWithPath("categoryName").type(JsonFieldType.STRING).description("생성할 카테고리 명"),
                  fieldWithPath("status").type(JsonFieldType.NUMBER)
                      .description("생성할 카테고리의 게시판 사용 상태값"),
                  fieldWithPath("createdUserId").type(JsonFieldType.NULL).ignored(),
                  fieldWithPath("createdAt").type(JsonFieldType.NULL).ignored(),
                  fieldWithPath("updatedUserId").type(JsonFieldType.NULL).ignored(),
                  fieldWithPath("updatedAt").type(JsonFieldType.NULL).ignored()),
              DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
              DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()));
    }

    @Test
    @DisplayName("카테고리의 정보 수정시, 수정한 경우, 상태값으로 OK와 결과값으로 true를 획득한다")
    void updateCategory_WhenUpdateSuccess_ThenReturnOkAndTrue() throws Exception {
      //given
      int categoryId = 1;
      CategoryUpdateDto requestDto = new CategoryUpdateDto().builder().categoryId(categoryId)
          .newCategoryName("Sample Update CategoryName").status(BoardStatusEnum.ACTIVE.getStatus())
          .build();

      MockHttpSession session = (MockHttpSession) SessionSampleObject.adminUserLoginSession;

      //mock
      when(categoryService.updateCategory(any(CategoryUpdateDto.class),
          any(HttpSession.class))).thenReturn(true);

      //when & then
      mockMvc.perform(RestDocumentationRequestBuilders.put("/board/category")
              .contentType(MediaType.APPLICATION_JSON_VALUE)
              .content(objectMapper.writeValueAsBytes(requestDto)).session(session))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
          .andExpect(jsonPath("$.result", Is.is(true)))
          .andDo(
              document(
                  "board/category/update",
                  DocumentSnippetsUtils.requestHeaders_ContentTypeApplicationJsonValue(),
                  requestFields(
                      fieldWithPath("categoryId").type(JsonFieldType.NUMBER)
                          .description("수정을 하고자 하는 카테고리 ID"),
                      fieldWithPath("newCategoryName").type(JsonFieldType.STRING)
                          .description("변경하려는 카테고리 이름"),
                      fieldWithPath("status").type(JsonFieldType.NUMBER).description("변경하려는 상태"),
                      fieldWithPath("createdAt").ignored(),
                      fieldWithPath("updatedAt").ignored(),
                      fieldWithPath("createdUserId").ignored(),
                      fieldWithPath("updatedUserId").ignored(),
                      fieldWithPath("newCategoryInfoValid").type(boolean.class)
                          .description(
                              "newCategoryName또는 status에 대한 정규검사로 두개가 모두 입력되지 않은 경우에는 수정이 불가능하다")
                  ),
                  DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                  DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
              )
          );
    }

    @Test
    @DisplayName("카테고리를 삭제할 때, 성공한 경우, 상태값으로 OK와 결과값으로 true를 획득한다")
    void deleteCategory_WhenDeleteSuccess_ThenReturnOkAndTrue() throws Exception {
      //given
      int requestCategoryId = 1;
      MockHttpSession session = (MockHttpSession) SessionSampleObject.adminUserLoginSession;

      //mock
      when(categoryService.deleteCategory(requestCategoryId, session)).thenReturn(true);

      //when & then
      mockMvc.perform(
              RestDocumentationRequestBuilders.delete("/board/category/{categoryId}", requestCategoryId)
                  .session(session))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
          .andExpect(jsonPath("$.result", Is.is(true)))
          .andDo(
              document(
                  "board/category/delete",
                  pathParameters(parameterWithName("categoryId").description("존재하는 카테고리 ID")),
                  DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                  DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
              )
          );
    }
  }

  @Nested
  @DisplayName("게시판 테스트 케이스")
  class BbsTest {

    private ResponseFieldsSnippet boardResponseFields() {
      return responseFields(
          fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("실행 결과의 상태값"),
          fieldWithPath("result").type(JsonFieldType.ARRAY).description("조회한 게시판 리스트"),
          fieldWithPath("result[].bbsId").type(JsonFieldType.NUMBER).description("게시판 ID"),
          fieldWithPath("result[].categoryId").type(JsonFieldType.NUMBER)
              .description("해당 게시판의 카테고리ID"),
          fieldWithPath("result[].bbsName").type(JsonFieldType.STRING).description("게시판 명"),
          fieldWithPath("result[].status").type(JsonFieldType.NUMBER).description("게시판의 사용 방식 상태값"),
          fieldWithPath("result[].createdUserId").type(JsonFieldType.STRING)
              .description("생성한 관리자 ID"),
          fieldWithPath("result[].createdAt").type(JsonFieldType.STRING).description("게시판 생성일"),
          fieldWithPath("result[].updatedUserId").type(JsonFieldType.STRING)
              .description("최종 수정한 관리자 ID"),
          fieldWithPath("result[].updatedAt").type(JsonFieldType.STRING).description("게시판 최종 수정일"));
    }


    @Test
    @DisplayName("게시판 리스트 조회시 카테고리ID를 제공한 경우, OK상태값과 해당 CategoryId에 포함된 게시판 리스트를 획득한다 ")
    void getBbsList_WhenGivenCategoryId_ThenReturnOkAndBbsList() throws Exception {
      //given
      int categoryId = 1;
      int status = BoardStatusEnum.ACTIVE.getStatus();
      String userId = SessionUtils.getSessionUserId(SessionSampleObject.adminUserLoginSession);

      List<BbsDto> responseList = BoardFixtureUtils.BbsListFixtureByCategoryIdAndStatusAndUserID(
          categoryId, status, userId);

      //mock
      when(categoryService.getBbsList(categoryId, status)).thenReturn(responseList);

      //when & then
      mockMvc.perform(
              RestDocumentationRequestBuilders.get("/board/bbsList/{categoryId}/{status}", categoryId,
                  status)).andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
          .andDo(
              document("board/bbs/getList", pathParameters(
                      parameterWithName("categoryId").description("게시판리스트를 조회하고자 하는 Category Id"),
                      parameterWithName("status").description("조회하고자 하는 게시판 리스트의 상태값")),
                  DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                  boardResponseFields()));

    }

    @Test
    @DisplayName("모든 게시판 리스트 조회 요청시, 상태값으로 OK와 모든 게시판 리스트를 획득한다")
    void getAllBbsList_WhenRequest_ThenReturnOkAndAllBbsList() throws Exception {
      //given
      String userId = SessionUtils.getSessionUserId(SessionSampleObject.adminUserLoginSession);
      List<BbsDto> BbsAllList = BoardFixtureUtils.BbsAllCategoryListFixtureByAdminUserID(userId);

      //mock
      when(categoryService.getAllBbsList()).thenReturn(BbsAllList);

      //when & then
      mockMvc.perform(RestDocumentationRequestBuilders.get("/board/bbsAllList"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value()))).andDo(
              document("board/bbs/getAllList",
                  DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                  boardResponseFields()));
    }

    @Test
    @DisplayName("게시판을 생성할 때, 추가가 이뤄졌다면, OK상태값과 결과값으로 true를 Return한다 ")
    void insertBbs_WhenInsertSuccess_ThenReturnOkAndTrue() throws Exception {
      //given
      MockHttpSession session = (MockHttpSession) SessionSampleObject.adminUserLoginSession;
      BbsInsertDto requestDto = new BbsInsertDto().builder()
          .categoryId(1)
          .bbsName("Sample Insert BbsName")
          .status(BoardStatusEnum.ACTIVE.getStatus())
          .build();

      //mock
      when(categoryService.insertBbs(any(BbsInsertDto.class), any(HttpSession.class)))
          .thenReturn(true);

      //when & then
      mockMvc.perform(
              RestDocumentationRequestBuilders.post("/board/bbs")
                  .session(session)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(requestDto))
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
          .andExpect(jsonPath("$.result", Is.is(true)))
          .andDo(
              document(
                  "board/bbs/add",
                  DocumentSnippetsUtils.requestHeaders_ContentTypeApplicationJsonValue(),
                  requestFields(
                      fieldWithPath("categoryId").description("생성하고자 하는 게시판의 카테고리ID"),
                      fieldWithPath("bbsName").description("생성하고자 하는 게시판명"),
                      fieldWithPath("status").description("게시판의 상태 값"),
                      fieldWithPath("createdUserId").ignored(),
                      fieldWithPath("createdAt").ignored(),
                      fieldWithPath("updatedUserId").ignored(),
                      fieldWithPath("updatedAt").ignored()
                  ),
                  DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                  DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
              )
          );

    }

    @Test
    @DisplayName("게시판을 수정할 때, 수정이 이뤄졌다면, Ok상태값과 결과값으로 true를 Return한다 ")
    void updateBbs_WhenUpdateSuccess_ThenReturnOkAndTrue() throws Exception {
      //given
      MockHttpSession session = (MockHttpSession) SessionSampleObject.adminUserLoginSession;
      BbsUpdateDto requestDto = new BbsUpdateDto().builder()
          .bbsId(1)
          .categoryId(1)
          .bbsName("Sample BbsUpdate Name")
          .status(BoardStatusEnum.ACTIVE.getStatus())
          .build();

      //mock
      when(categoryService.updateBbs(any(BbsUpdateDto.class), any(HttpSession.class)))
          .thenReturn(true);

      mockMvc.perform(
              RestDocumentationRequestBuilders
                  .put("/board/bbs")
                  .contentType(MediaType.APPLICATION_JSON_VALUE)
                  .content(objectMapper.writeValueAsBytes(requestDto))
                  .session(session)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
          .andExpect(jsonPath("$.result", Is.is(true)))
          .andDo(
              document(
                  "board/bbs/update",
                  DocumentSnippetsUtils.requestHeaders_ContentTypeApplicationJsonValue(),
                  requestFields(
                      fieldWithPath("bbsId").type(JsonFieldType.NUMBER)
                          .description("수정하고자 하는 기존 게시판 ID"),
                      fieldWithPath("categoryId").type(JsonFieldType.NUMBER)
                          .description("변경하고자 하는 카테고리 ID"),
                      fieldWithPath("bbsName").type(JsonFieldType.STRING)
                          .description("변경하고자 하는 게시판명"),
                      fieldWithPath("status").type(JsonFieldType.NUMBER)
                          .description("변경하고자 하는 게시판 상태값"),
                      fieldWithPath("validUpdateData").type(JsonFieldType.BOOLEAN)
                          .description("카테고리ID, 게시판명, 상태값 전부 변경사항이 없는 경우 변경이 불가능하다."),
                      fieldWithPath("createdUserId").ignored(),
                      fieldWithPath("createdAt").ignored(),
                      fieldWithPath("updatedUserId").ignored(),
                      fieldWithPath("updatedAt").ignored()
                  ),
                  DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                  DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
              )
          );
    }

    @Test
    @DisplayName("게시판을 삭제할 때, 삭제가 이뤄졌다면, Ok상태값과 결과값으로 true를 Return한다 ")
    void deleteBbs_WhenDeleteSuccess_ThenReturnOkAndTrue() throws Exception {
      //given
      int bbsId = 1;
      MockHttpSession session = (MockHttpSession) SessionSampleObject.adminUserLoginSession;

      //mock
      when(categoryService.deleteBbs(bbsId, session)).thenReturn(true);

      //when & then
      mockMvc.perform(
              RestDocumentationRequestBuilders.delete("/board/bbs/{bbsId}", bbsId).session(session))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
          .andExpect(jsonPath("$.result", Is.is(true)))
          .andDo(
              document(
                  "board/bbs/delete",
                  pathParameters(parameterWithName("bbsId").description("삭제를 하고자 하는 게시판 ID")),
                  DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                  DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
              )
          );
    }
  }
}