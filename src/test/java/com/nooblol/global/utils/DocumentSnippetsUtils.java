package com.nooblol.global.utils;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

//mockMvc과정에서 너무 많은 사용을 하는 Snippet에 대해서 사전에 생성을 함.
public interface DocumentSnippetsUtils {

  static RequestHeadersSnippet requestHeaders_ContentTypeApplicationJsonValue() {
    return requestHeaders(
        headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
    );
  }


  static ResponseHeadersSnippet responseHeaders_ContentTypeApplicationJsonValue() {
    return responseHeaders(
        headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
    );
  }

  static ResponseFieldsSnippet responseFields_IsOkStatusAndResultTrue() {
    return responseFields(
        fieldWithPath("resultCode").type(JsonFieldType.NUMBER).description("실행 결과의 상태값"),
        fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("실행 성공 유무")
    );
  }
}
