package com.nooblol.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nooblol.global.utils.DocumentSnippetsUtils;
import com.nooblol.global.utils.RestDocConfiguration;
import com.nooblol.global.utils.SessionSampleObject;
import com.nooblol.global.utils.SessionUtils;
import com.nooblol.user.dto.LetterDto;
import com.nooblol.user.dto.LetterInsertRequestDto;
import com.nooblol.user.mapper.LetterMapper;
import com.nooblol.user.service.LetterService;
import com.nooblol.user.utils.LetterConstants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(LetterController.class)
@Import(RestDocConfiguration.class)
@AutoConfigureRestDocs
class LetterControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  LetterService letterService;

  @Mock
  LetterMapper letterMapper;

  @Test
  @DisplayName("쪽지를 조회 할 때 실제 존재하는 쪽지이며 수신자 또는 발신자인 경우에 쪽지의 데이터를 획득한다.")
  void getLetter_WhenRequireUserIsToOrFromLetterUser_ThenReturnLetterContent() throws Exception {
    //given
    int reqLetterId = 1;
    MockHttpSession reqSession = (MockHttpSession) SessionSampleObject.authUserLoginSession;

    //mock
    when(letterService.getLetter(reqLetterId, reqSession))
        .thenReturn(
            new LetterDto().builder()
                .letterId(reqLetterId)
                .letterTitle("Sample Letter Title")
                .letterContent("Sample Letter Content")
                .toUserId("testUserId")
                .fromUserId(SessionUtils.getSessionUserId(reqSession))
                .toStatus(LetterConstants.LETTER_STATUS_READ)
                .fromStatus(LetterConstants.LETTER_STATUS_READ)
                .createdAt(LocalDateTime.now())
                .build()
        );

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/letter/{letterId}", reqLetterId)
                .session(reqSession)
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andDo(
            document(
                "user/letter/getLetter",
                pathParameters(
                    parameterWithName("letterId").description("조회 할 쪽지의 ID")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                responseFields(
                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER)
                        .description("실행 결과의 상태값"),
                    fieldWithPath("result").type(JsonFieldType.OBJECT).description("조회한 쪽지 데이터"),
                    fieldWithPath("result.letterId").type(JsonFieldType.NUMBER)
                        .description("쪽지 ID"),
                    fieldWithPath("result.letterTitle").type(JsonFieldType.STRING)
                        .description("쪽지 제목"),
                    fieldWithPath("result.letterContent").type(JsonFieldType.STRING)
                        .description("쪽지 내용"),
                    fieldWithPath("result.toUserId").type(JsonFieldType.STRING)
                        .description("쪽지 수신자"),
                    fieldWithPath("result.toStatus").type(JsonFieldType.NUMBER)
                        .description("수신자의 쪽지 상태"),
                    fieldWithPath("result.fromUserId").type(JsonFieldType.STRING)
                        .description("쪽지 발신자"),
                    fieldWithPath("result.fromStatus").type(JsonFieldType.NUMBER)
                        .description("발신자의 쪽지 상태"),
                    fieldWithPath("result.createdAt").type(JsonFieldType.STRING)
                        .description("쪽지 발신일")
                )
            )
        );
  }

  @Test
  @DisplayName("쪽지함을 조회 할 때, 조회하고자 하는 쪽지 타입에 따라 쪽지 리스트를 획득한다.")
  void getLetterToList_WhenRequireType_ThenReturnLetterReqTypeList() throws Exception {
    //given
    MockHttpSession reqSession = (MockHttpSession) SessionSampleObject.authUserLoginSession;

    String type = LetterConstants.LETTER_TYPE_FROM;
    int page = 1;
    int limit = 30;

    List<LetterDto> letterList = new ArrayList<LetterDto>();

    for (int i = 1; i < 4; i++) {
      letterList.add(
          new LetterDto().builder()
              .letterId(i)
              .letterTitle("Sample Letter Title" + i)
              .letterContent("Sample Letter Content" + i)
              .toUserId("testUserId")
              .fromUserId(SessionUtils.getSessionUserId(reqSession))
              .toStatus(LetterConstants.LETTER_STATUS_READ)
              .fromStatus(LetterConstants.LETTER_STATUS_READ)
              .createdAt(LocalDateTime.now())
              .build()
      );
    }

    //mock
    when(letterService.getLetterListByUserId(any())).thenReturn(letterList);

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/letter/list/{type}", type)
                .param("page", String.valueOf(page))
                .param("limit", String.valueOf(limit))
                .session(reqSession)
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andDo(
            document(
                "user/letter/getLetterList",
                pathParameters(
                    parameterWithName("type").description("조회 할 쪽지의 ID")
                ),
                requestParameters(
                    parameterWithName("page").description("요청하려는 Page"),
                    parameterWithName("limit").description("최대 limit 갯수만큼의 사용자 정보 반환")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                responseFields(
                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER)
                        .description("실행 결과의 상태값"),
                    fieldWithPath("result[]").type(JsonFieldType.ARRAY).description("조회한 쪽지 데이터"),
                    fieldWithPath("result[].letterId").type(JsonFieldType.NUMBER)
                        .description("쪽지 ID"),
                    fieldWithPath("result[].letterTitle").type(JsonFieldType.STRING)
                        .description("쪽지 제목"),
                    fieldWithPath("result[].letterContent").type(JsonFieldType.STRING)
                        .description("쪽지 내용"),
                    fieldWithPath("result[].toUserId").type(JsonFieldType.STRING)
                        .description("쪽지 수신자"),
                    fieldWithPath("result[].toStatus").type(JsonFieldType.NUMBER)
                        .description("수신자의 쪽지 상태"),
                    fieldWithPath("result[].fromUserId").type(JsonFieldType.STRING)
                        .description("쪽지 발신자"),
                    fieldWithPath("result[].fromStatus").type(JsonFieldType.NUMBER)
                        .description("발신자의 쪽지 상태"),
                    fieldWithPath("result[].createdAt").type(JsonFieldType.STRING)
                        .description("쪽지 발신일")
                )
            )
        );
  }


  @Test
  @DisplayName("쪽지 발송을 할 때, 정상적으로 발송이 이뤄진 경우, OK상태값과 결과값으로 True를 획득한다.")
  void insertLetter_WhenInsertSuccess_ThenReturnTrueAndOk() throws Exception {
    //given
    LetterInsertRequestDto requestToLetterDto = new LetterInsertRequestDto().builder()
        .letterTitle("Sample Insert Letter")
        .letterContent("Sample Content Letter")
        .toUserId("toSampleUserId")
        .build();

    MockHttpSession session = (MockHttpSession) SessionSampleObject.authUserLoginSession;

    //mock
    when(
        letterService.insertLetter(any(LetterInsertRequestDto.class), any(HttpSession.class))
    ).thenReturn(true);

    //when & then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/letter/")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestToLetterDto))
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document(
                "user/letter/addLetter",
                DocumentSnippetsUtils.requestHeaders_ContentTypeApplicationJsonValue(),
                requestFields(
                    fieldWithPath("letterTitle").type(JsonFieldType.STRING).description("쪽지 제목"),
                    fieldWithPath("letterContent").type(JsonFieldType.STRING).description("쪽지 내용"),
                    fieldWithPath("toUserId").type(JsonFieldType.STRING).description("수신받을 사용자 ID")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
            )
        );
  }

  @Test
  @DisplayName("쪽지를 삭제 할 때, 해당 쪽지가 발신자또는 수신자인 경우, 해당 타입의 Status를 변경하 OK상태값과 결과값으로 True를 획득한다")
  void deleteLetter_WhenDeleteSuccess_ThenReturnTrueAndOk() throws Exception {
    //given
    int letterId = 1;
    String type = LetterConstants.LETTER_TYPE_FROM;
    MockHttpSession session = (MockHttpSession) SessionSampleObject.authUserLoginSession;

    //mock
    when(letterService.deleteLetter(any(LetterDto.class), any(HttpSession.class))).thenReturn(true);

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete("/letter/{type}/{letterId}", type, letterId)
                .session(session))
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document(
                "user/letter/delete",
                pathParameters(
                    parameterWithName("type").description("Type이 수신인지 발신인지 구분 값"),
                    parameterWithName("letterId").description("삭제하고자 하는 쪽지 ID")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
            )
        );
  }
}