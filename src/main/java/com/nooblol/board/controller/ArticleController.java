package com.nooblol.board.controller;

import com.nooblol.board.dto.ArticleDto;
import com.nooblol.board.service.ArticleService;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.SessionUtils;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

  /**
   * articleId의 게시물 정보와 현재 요청한 사용자의 권한을 같이 Return한다
   *
   * @param articleId
   * @param session
   * @return
   */
  @GetMapping("/{articleId}")
  public ResponseDto getArticle(
      @PathVariable int articleId, HttpSession session
  ) {
    ArticleDto article = articleService.getArticleInfo(
        articleId, SessionUtils.getSessionUserId(session)
    );

    ResponseDto result = ResponseEnum.OK.getResponse();
    result.setResult(article);
    return result;
  }
}
