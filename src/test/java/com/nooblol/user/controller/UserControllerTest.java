package com.nooblol.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.DocumentSnippetsUtils;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.ResponseFixtureUtils;
import com.nooblol.global.utils.RestDocConfiguration;
import com.nooblol.global.utils.SessionSampleObject;
import com.nooblol.global.utils.UserSampleObject;
import com.nooblol.user.dto.UserInfoUpdateDto;
import com.nooblol.user.dto.UserLoginDto;
import com.nooblol.user.dto.UserSignOutDto;
import com.nooblol.user.dto.UserSignUpRequestDto;
import com.nooblol.user.service.UserInfoService;
import com.nooblol.user.service.UserSignOutService;
import com.nooblol.user.service.UserSignUpService;
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

//TODO : 22. 09. 29 : any()가 아닌 requestDto를 설정했으나, ResponseDto가 Null로 반환이 되고있슴ㅠㅠㅠㅠ.
//로그아웃, 인증메일 재발송, 권한 변경기능 제외
@WebMvcTest(UserController.class)
@Import(RestDocConfiguration.class)
@AutoConfigureRestDocs
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  UserSignUpService userSignUpService;

  @MockBean
  UserSignOutService userSignOutService;

  @MockBean
  UserInfoService userInfoService;

  @Test
  @DisplayName("회원가입을 요청 할 때, 회원가입이 이뤄진 경우 Ok상태값과 결과값으로 True를 획득한다")
  void userSignUp_WhenSignUpSuccess_ThenReturnTrueAndOk() throws Exception {
    //given
    UserSignUpRequestDto requestDto = UserFixtureUtils.getCreateUserFixture();
    ResponseDto mockReturnDto = ResponseFixtureUtils.getResultCodeIsOkAndResultTrueFixture();

    //mock
    when(userSignUpService.signUpUser(any())).thenReturn(mockReturnDto);

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders.post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestDto))
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document(
                "user/signUp",
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
  @DisplayName("회원정보를 수정 할 때, 수정이 이뤄진 경우 Ok상태값과 결과값으로 True를 획득한다")
  void userUpdate_WhenUserInfoUpdateSuccess_ThenReturnTrueAndOk() throws Exception {
    //given
    UserInfoUpdateDto requestDto = UserFixtureUtils.getUserInfoUpdateRequestFixture();

    //mock
    when(userInfoService.updateUserInfo(any()))
        .thenReturn(ResponseFixtureUtils.getResultCodeIsOkAndResultTrueFixture());

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders.post("/user/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestDto))
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document(
                "user/infoUpdate",
                DocumentSnippetsUtils.requestHeaders_ContentTypeApplicationJsonValue(),
                requestFields(
                    fieldWithPath("userId").type(JsonFieldType.STRING)
                        .description("수정하고자 하는 사용자 ID"),
                    fieldWithPath("userEmail").type(JsonFieldType.STRING)
                        .description("수정하고자 하는 사용자 Email"),
                    fieldWithPath("orgUserName").type(JsonFieldType.STRING)
                        .description("현재 사용중인 사용자 이름"),
                    fieldWithPath("newUserName").type(JsonFieldType.STRING)
                        .description("변경하고자 하는 사용자 이름"),
                    fieldWithPath("orgPassword").type(JsonFieldType.STRING)
                        .description("현재 사용중인 패스워드"),
                    fieldWithPath("newPassword").type(JsonFieldType.STRING)
                        .description("변경하고자 하는 패스워드"),
                    fieldWithPath("userRole").type(JsonFieldType.NUMBER).description("현재 사용자 Role"),
                    fieldWithPath("reqUserInfoUpdateRoleValidation").description(
                        "사용자 이름 또는 사용자 명에 대한 검증으로, 둘중 한개는 입력이 되어야 한다."),
                    fieldWithPath("updatedAt").ignored()
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
            )
        );
  }

  @Test
  @DisplayName("회원 탈퇴시, 삭제작업이 진행이 된 경우 Ok상태값과 결과값으로 True를 획득한다")
  void userSignOut_WhenSignOutSuccess_ThenReturnTrueAndOk() throws Exception {
    //given
    UserSignOutDto requestDto = UserFixtureUtils.getUserSignOutFixture();

    //mock
    when(userSignOutService.signOutUser(any()))
        .thenReturn(ResponseFixtureUtils.getResultCodeIsOkAndResultTrueFixture());

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/user/signout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestDto))
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document(
                "user/signOut",
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
  @DisplayName("로그인 요청시, 사용자 정보가 일치하면, Ok상태값과 해당 사용자에 대한 정보를 획득한다.")
  void userLogin_WhenIsExistsEmailAndPassword_ThenReturnUserInfoAndOk() throws Exception {
    //given
    UserLoginDto requestDto = UserFixtureUtils.getUserLoginFixture();
    MockHttpSession mockEmptySession = new MockHttpSession();

    ResponseDto mockResponseDto = ResponseEnum.OK.getResponse();
    mockResponseDto.setResult(UserSampleObject.authUserDto);

    //mock
    when(userInfoService.userLogin(any(), any()))
        .thenReturn(mockResponseDto);

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestDto))
                .session(mockEmptySession)
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andDo(print())
        .andDo(
            document(
                "user/login",
                DocumentSnippetsUtils.requestHeaders_ContentTypeApplicationJsonValue(),
                requestFields(
                    fieldWithPath("userEmail").type(JsonFieldType.STRING)
                        .description("로그인하려는 사용자 Email"),
                    fieldWithPath("userPassword").type(JsonFieldType.STRING)
                        .description("요청한Email로 설정한 패스워드")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                responseFields(
                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER)
                        .description("실행 결과의 상태값"),
                    fieldWithPath("result").type(JsonFieldType.OBJECT).description("로그인한 사용자 정보"),
                    fieldWithPath("result.userId").type(JsonFieldType.STRING)
                        .description("사용자 고유 ID"),
                    fieldWithPath("result.userEmail").type(JsonFieldType.STRING)
                        .description("사용자 Email"),
                    fieldWithPath("result.userName").type(JsonFieldType.STRING)
                        .description("사용자 이름"),
                    fieldWithPath("result.userRole").type(JsonFieldType.NUMBER)
                        .description("계정 권한"),
                    fieldWithPath("result.level").type(JsonFieldType.NUMBER)
                        .description("계정 커뮤니티 레벨"),
                    fieldWithPath("result.exp").type(JsonFieldType.NUMBER)
                        .description("계정 커뮤니티 경험치 량"),
                    fieldWithPath("result.createdAt").type(JsonFieldType.STRING)
                        .description("계정 최초 생성일"),
                    fieldWithPath("result.updatedAt").type(JsonFieldType.STRING)
                        .description("계정 최종 수정일"),
                    fieldWithPath("result.userPassword").ignored()
                )
            )
        );
  }

  @Test
  @DisplayName("로그아웃 요청을 할 때, 기능이 전부 실행되면, Ok상태값과 결과값으로 True를 획득한다")
  void userLogout_WhenIsSuccess_ThenReturnTrueAndOk() throws Exception {
    //given
    MockHttpSession session = (MockHttpSession) SessionSampleObject.authUserLoginSession;

    //mock
    when(userInfoService.userLogout(session))
        .thenReturn(ResponseFixtureUtils.getResultCodeIsOkAndResultTrueFixture());

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders.post("/user/logout")
                .session(session)
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document(
                "user/logout",
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
            )
        );

  }

  @Test
  @DisplayName("사용자 인증메일의 재발송 요청시, 실제 인증이 필요한 사용자인 경우, 메일을 발송한 이후 Ok상태값과 결과값으로 True를 획득한다")
  void resendAuthMail_WhenIsSuccess_ThenReturnTrueAndStatusOk() throws Exception {
    //given
    String requestEmail = UserFixtureUtils.fixtureAuthUserName;

    //mock
    when(userSignUpService.reSendSignUpUserMail(requestEmail.trim())).thenReturn(
        ResponseFixtureUtils.getResultCodeIsOkAndResultTrueFixture());

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders.get("/user/resend-authmail/{email}", requestEmail)
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document(
                "user/resendAuthMail",
                pathParameters(
                    parameterWithName("email").description("재수신을 받고자 하는 사용자 이메일")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
            )
        );

  }

  @Test
  @DisplayName("사용자 인증이 필요한 계정으로 요청이 들어온 경우, 상태값이 정상적으로 수정이 이뤄지면 Ok상태값과 결과값으로 True를 획득한다")
  void authUserByMail_WhenIsSuccess_ThenReturnTrueAndStatusOk() throws Exception {
    //given
    String requestUserId = UserFixtureUtils.fixtureAuthUserId;

    //mock
    when(userSignUpService.changeRoleAuthUser(requestUserId)).thenReturn(
        ResponseFixtureUtils.getResultCodeIsOkAndResultTrueFixture());

    //when & then
    mockMvc.perform(
            RestDocumentationRequestBuilders.get("/user/auth/{userId}", requestUserId)
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andExpect(jsonPath("$.result", Is.is(true)))
        .andDo(
            document(
                "user/resendAuthMail",
                pathParameters(
                    parameterWithName("userId").description("인증권한을 일반 사용자로 변경하려는 사용자ID")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                DocumentSnippetsUtils.responseFields_IsOkStatusAndResultTrue()
            )
        );


  }
}