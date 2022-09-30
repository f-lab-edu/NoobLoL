package com.nooblol.board.controller;

import com.nooblol.board.dto.ArticleDto;
import com.nooblol.board.dto.ArticleInsertRequestDto;
import com.nooblol.board.dto.ArticleUpdateRequestDto;
import com.nooblol.board.service.ArticleService;
import com.nooblol.global.annotation.UserLoginCheck;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.ResponseUtils;
import com.nooblol.global.utils.SessionUtils;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
   * articleId의 게시물 정보와 현재 요청한 사용자의 권한을 같이 Return한다 Session에 로그인정보가 없는 경우에는 게시물에 대한 정보 수정을 주는 권한을
   * Guest로 설정한다
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

    return ResponseUtils.makeToResponseOkDto(article);
  }

  /**
   * 게시물 등록
   *
   * @param articleDto
   * @return
   */
  @UserLoginCheck
  @PostMapping("/")
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
        .build();

    boolean upsertResult = articleService.upsertArticle(upsertArticle, session, true);

    return ResponseUtils.makeToResponseOkDto(upsertResult);
  }

  /**
   * 게시물 수정
   *
   * @param articleDto
   * @return
   */
  @UserLoginCheck
  @PutMapping("/")
  public ResponseDto updateArticle(
      @Valid @RequestBody ArticleUpdateRequestDto articleDto, HttpSession session
  ) {
    ArticleDto upsertArticle = new ArticleDto().builder()
        .articleId(articleDto.getArticleId())
        .bbsId(articleDto.getBbsId())
        .articleTitle(articleDto.getArticleTitle())
        .articleContent(articleDto.getArticleContent())
        .status(articleDto.getStatus())
        .build();

    boolean upsertResult = articleService.upsertArticle(upsertArticle, session, false);

    return ResponseUtils.makeToResponseOkDto(upsertResult);
  }

  /**
   * 게시물 삭제
   *
   * @param articleId
   * @param session
   * @return
   */
  @UserLoginCheck
  @DeleteMapping("/{articleId}")
  public ResponseDto deleteArticle(@PathVariable int articleId, HttpSession session) {
    return ResponseUtils.makeToResponseOkDto(articleService.deleteArticle(articleId, session));
  }
}
