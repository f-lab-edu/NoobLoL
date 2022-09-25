package com.nooblol.board.controller;

import com.nooblol.board.dto.ArticleDto;
import com.nooblol.board.dto.ArticleInsertRequestDto;
import com.nooblol.board.dto.ArticleUpdateRequestDto;
import com.nooblol.board.service.ArticleService;
import com.nooblol.global.annotation.UserLoginCheck;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.CommonUtils;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.SessionUtils;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    return CommonUtils.makeToResponseOkDto(article);
  }

  /**
   * 게시물 등록
   *
   * @param articleDto
   * @return
   */
  @UserLoginCheck
  @PostMapping("/insert")
  public ResponseDto insertArticle(
      @Valid @RequestBody ArticleInsertRequestDto articleDto, HttpSession session
  ) {
    ArticleDto upsertArticle = new ArticleDto().builder()
        .articleId(articleService.getNewArticleId())
        .bbsId(articleDto.getBbsId())
        .articleTitle(articleDto.getArticleTitle())
        .articleContent(articleDto.getArticleContent())
        .articleReadCount(articleDto.getArticleReadCount())
        .status(articleDto.getStatus())
        .createdUserId(SessionUtils.getSessionUserId(session))
        .createdAt(articleDto.getCreatedAt())
        .updatedAt(articleDto.getUpdatedAt())
        .build();

    boolean upsertResult = articleService.upsertArticle(upsertArticle, session, true);

    return CommonUtils.makeToResponseOkDto(upsertResult);
  }

  /**
   * 게시물 수정
   *
   * @param articleDto
   * @return
   */
  @UserLoginCheck
  @PostMapping("/update")
  public ResponseDto updateArticle(
      @Valid @RequestBody ArticleUpdateRequestDto articleDto, HttpSession session
  ) {
    ArticleDto upsertArticle = new ArticleDto().builder()
        .articleId(articleDto.getArticleId())
        .bbsId(articleDto.getBbsId())
        .articleTitle(articleDto.getArticleTitle())
        .articleContent(articleDto.getArticleContent())
        .status(articleDto.getStatus())
        .updatedAt(articleDto.getUpdatedAt())
        .build();

    boolean upsertResult = articleService.upsertArticle(upsertArticle, session, false);

    return CommonUtils.makeToResponseOkDto(upsertResult);
  }

  /**
   * @param articleId
   * @param session
   * @return
   */
  @UserLoginCheck
  @DeleteMapping("/delete/{articleId}")
  public ResponseDto deleteArticle(
      @PathVariable int articleId, HttpSession session
  ) {
    boolean deleteResult = articleService.deleteArticle(articleId, session);

    return CommonUtils.makeToResponseOkDto(deleteResult);
  }


  /**
   * 파라미터로 제공한 게시물의 추천, 비추천 갯수를 Return한다
   *
   * @param articleId
   * @return
   */
  @GetMapping("/status/{articleId}")
  public ResponseDto likeAndNotLikeArticle(@PathVariable int articleId) {
    return CommonUtils.makeToResponseOkDto(articleService.likeAndNotListStatus(articleId));
  }

  /**
   * 게시물 추천
   *
   * @param articleId
   * @param session
   * @return
   */
  @UserLoginCheck
  @GetMapping("/like/{articleId}")
  public ResponseDto likeArticle(@PathVariable int articleId, HttpSession session) {
    return CommonUtils.makeToResponseOkDto(articleService.likeArticle(articleId, session));
  }

  /**
   * 게시물 비추천
   *
   * @param articleId
   * @param session
   * @return
   */
  @UserLoginCheck
  @GetMapping("/notLike/{articleId}")
  public ResponseDto notLikeArticle(@PathVariable int articleId, HttpSession session) {
    return CommonUtils.makeToResponseOkDto(articleService.notLikeArticle(articleId, session));
  }
}
