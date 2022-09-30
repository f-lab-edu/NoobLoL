package com.nooblol.board.controller;

import com.nooblol.board.service.ArticleStatusService;
import com.nooblol.global.annotation.UserLoginCheck;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.ResponseUtils;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/article/status")
@RequiredArgsConstructor
public class ArticleStatusController {

  private final ArticleStatusService articleStatusService;

  /**
   * 파라미터로 제공한 게시물의 추천, 비추천 갯수를 Return한다
   *
   * @param articleId
   * @return
   */
  @GetMapping("/{articleId}")
  public ResponseDto likeAndNotLikeArticle(@PathVariable int articleId) {
    return ResponseUtils.makeToResponseOkDto(articleStatusService.likeAndNotListStatus(articleId));
  }

  /**
   * 게시물 추천
   *
   * @param articleId
   * @param session
   * @return
   */
  @UserLoginCheck
  @PostMapping("/like/{articleId}")
  public ResponseDto likeArticle(@PathVariable int articleId, HttpSession session) {
    return ResponseUtils.makeToResponseOkDto(articleStatusService.likeArticle(articleId, session));
  }

  /**
   * 게시물 비추천
   *
   * @param articleId
   * @param session
   * @return
   */
  @UserLoginCheck
  @PostMapping("/notLike/{articleId}")
  public ResponseDto notLikeArticle(@PathVariable int articleId, HttpSession session) {
    return ResponseUtils.makeToResponseOkDto(
        articleStatusService.notLikeArticle(articleId, session));
  }
}
