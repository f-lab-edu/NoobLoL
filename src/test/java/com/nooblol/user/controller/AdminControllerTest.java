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
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.DocumentSnippetsUtils;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.ResponseFixtureUtils;
import com.nooblol.global.utils.RestDocConfiguration;
import com.nooblol.global.utils.SessionSampleObject;
import com.nooblol.user.dto.UserSignOutDto;
import com.nooblol.user.dto.UserSignUpRequestDto;
import com.nooblol.user.service.AdminService;
import com.nooblol.user.utils.UserFixtureUtils;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@WebMvcTest(AdminController.class)
@Import(RestDocConfiguration.class)
@AutoConfigureRestDocs
class AdminControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  AdminService adminService;

  MockHttpSession adminSession = (MockHttpSession) SessionSampleObject.adminUserLoginSession;

  @Test
  @DisplayName("관리자 계정 추가시, Ok상태값과 결과값으로 True를 획득한다")
  void signUpAdminMember_WhenAddAdminSuccess_ThenReturnTrueAndOk() throws Exception {
    //given
    UserSignUpRequestDto requestDto = UserFixtureUtils.getCreateUserFixture();

    //TODO [22. 09. 29] : Null발생
    //mock
    when(adminService.addAdminMember(any()))
        .thenReturn(ResponseFixtureUtils.getResultCodeIsOkAndResultTrueFixture());

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders.post("/admin/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(requestDto))
                .session(adminSession)
        ).andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document(
                "admin/signup",
                DocumentSnippetsUtils.requestHeaders_ContentTypeApplicationJsonValue(),
                requestFields(
                    fieldWithPath("userId").ignored(),
                    fieldWithPath("userRole").ignored(),
                    fieldWithPath("createdAt").ignored(),
                    fieldWithPath("updatedAt").ignored(),
                    fieldWithPath("userEmail").type(JsonFieldType.STRING)
                        .description("로그인에 사용하고자 하는 Email"),
                    fieldWithPath("userName").type(JsonFieldType.STRING).description("사용자 명"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
            )
        );

  }

  @Test
  @DisplayName("관리자 계정 삭제를 한 경우, Ok상태값과 결과값으로 True를 획득한다")
  void signOutAdminMember_WhenSignOutAdminSuccess_ThenReturnTrueAndOk() throws Exception {
    //given
    UserSignOutDto requestDto = UserFixtureUtils.getUserSignOutFixture();

    //TODO [22. 09. 29] : Null발생
    //mock
    when(adminService.deleteAdminMember(any()))
        .thenReturn(ResponseFixtureUtils.getResultCodeIsOkAndResultTrueFixture());

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders.post("/admin/signout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestDto))
                .session(adminSession)
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document(
                "user/admin/signOut",
                DocumentSnippetsUtils.requestHeaders_ContentTypeApplicationJsonValue(),
                requestFields(
                    fieldWithPath("userId").type(JsonFieldType.STRING)
                        .description("삭제하려는 사용자 ID"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("삭제하려는 사용자계정의 패스워드")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
            )
        );
  }

  @Test
  @DisplayName("일반 사용자 계정을 강제 삭제할 때, 삭제에 성공한 경우 결과값으로 True를 획득한다")
  void deleteForcedUser_WhenDeleteSuccess_ThenReturnTrueAndOk() throws Exception {
    //given
    String requestUserId = UserFixtureUtils.fixtureAuthUserId;

    //mock
    when(adminService.forceDeleteUser(requestUserId))
        .thenReturn(ResponseFixtureUtils.getResultCodeIsOkAndResultTrueFixture());

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete("/admin/forceUserDelete/{deleteUserId}", requestUserId)
                .session(adminSession)
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document(
                "admin/forceUserDelete",
                pathParameters(
                    parameterWithName("deleteUserId")
                        .description("강제 삭제하고자 하는 사용자 ID")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
            )
        );
  }

  @Test
  @DisplayName("관리자가 사용자에 리스트에 대해서 조회할경우, 조회한 결과를 획득한다")
  void getAllUserList_WhenIsRequestUserIsAdmin_ThenReturnUserList() throws Exception {
    //given
    int pageNum = 1;
    int limitNum = 30;

    ResponseDto mockReturnDto = ResponseEnum.OK.getResponse();
    mockReturnDto.setResult(UserFixtureUtils.getUserListFixture());

    //mock
    when(adminService.getAllUserList(pageNum * limitNum, limitNum))
        .thenReturn(mockReturnDto);

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/admin/userList")
                .param("page", String.valueOf(pageNum))
                .param("limit", String.valueOf(limitNum))
                .session(adminSession)
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andDo(
            document(
                "admin/userList",
                requestParameters(
                    parameterWithName("page").description("요청하려는 Page"),
                    parameterWithName("limit").description("최대 limit 갯수만큼의 사용자 정보 반환")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                responseFields(
                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER)
                        .description("실행 결과의 상태값"),
                    fieldWithPath("result[]").type(JsonFieldType.ARRAY).description("사용자 정보 리스트"),
                    fieldWithPath("result[].userId").type(JsonFieldType.STRING)
                        .description("사용자 고유 ID"),
                    fieldWithPath("result[].userEmail").type(JsonFieldType.STRING)
                        .description("사용자 Email"),
                    fieldWithPath("result[].userName").type(JsonFieldType.STRING)
                        .description("사용자 이름"),
                    fieldWithPath("result[].userRole").type(JsonFieldType.NUMBER)
                        .description("계정 권한"),
                    fieldWithPath("result[].level").type(JsonFieldType.NUMBER)
                        .description("계정 커뮤니티 레벨"),
                    fieldWithPath("result[].exp").type(JsonFieldType.NUMBER)
                        .description("계정 커뮤니티 경험치 량"),
                    fieldWithPath("result[].createdAt").type(JsonFieldType.STRING)
                        .description("계정 최초 생성일"),
                    fieldWithPath("result[].updatedAt").type(JsonFieldType.STRING)
                        .description("계정 최종 수정일"),
                    fieldWithPath("result[].userPassword").ignored()
                )
            )
        );

  }

  @Test
  @DisplayName("특정 사용자의 권한을 AuthUser로 변경할 때, 관리자가 요청한 경우에는, 변경후 결과값으로 True를 획득한다")
  void changeToActiveUser_WhenIsRequestUserIsAdmin_ThenReturnTrue() throws Exception {
    //given
    String requestUserId = UserFixtureUtils.fixtureAuthUserId;

    //mock
    when(adminService.changeToActiveUser(requestUserId))
        .thenReturn(ResponseFixtureUtils.getResultCodeIsOkAndResultTrueFixture());

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("/admin/userChangeToActive/{changeUserId}", requestUserId)
                .session(adminSession)
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document(
                "user/admin/changeToAuthUser",
                pathParameters(
                    parameterWithName("changeUserId")
                        .description("UserRole의 상태를 Suspension으로 변경하고자 하는 사용자ID")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
            )
        );
  }

  @Test
  @DisplayName("특정 사용자의 권한을 Suspension로 변경할 때, 관리자가 요청한 경우에는, 변경후 결과값으로 True를 획득한다")
  void changeToSuspensionUser_WhenIsRequestUserIsAdmin_ThenReturnTrue() throws Exception {
    //given
    String requestUserId = UserFixtureUtils.fixtureAuthUserId;

    //mock
    when(adminService.changeToSuspensionUser(requestUserId)).thenReturn(
        ResponseFixtureUtils.getResultCodeIsOkAndResultTrueFixture());

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("/admin/userChangeToSuspension/{changeUserId}", requestUserId)
                .session(adminSession)
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document(
                "user/admin/changeToSuspensionUser",
                pathParameters(
                    parameterWithName("changeUserId")
                        .description("UserRole의 상태를 Suspension으로 변경하고자 하는 사용자ID")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
            )
        );

  }
}