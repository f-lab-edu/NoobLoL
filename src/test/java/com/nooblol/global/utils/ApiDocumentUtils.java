package com.nooblol.global.utils;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;


// 우아한 형제들 기술블로그 ApiDocumentUtils.java 소스 참고 : https://techblog.woowahan.com/2597/
// 기존에 사용하는 소스였으나, RestDocConfiguration에서 기본적으로 셋팅하게 수정함.
public interface ApiDocumentUtils {

  //PrettyPrint만 적용하기
  static OperationRequestPreprocessor getDocumentRequest() {
    return Preprocessors.preprocessRequest(
        Preprocessors.modifyUris()
        //.scheme("https")
        //.host("docs.api.com")
        //.removePort(),
        , Preprocessors.prettyPrint()
    );
  }

  static OperationResponsePreprocessor getDocumentResponse() {
    return Preprocessors.preprocessResponse(Preprocessors.prettyPrint());
  }
}
