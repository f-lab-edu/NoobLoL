package com.nooblol.board.controller;

import com.nooblol.board.dto.ReplyRequestDto.ReplyInsertDto;
import com.nooblol.board.dto.ReplyRequestDto.ReplyUpdateDto;
import com.nooblol.board.service.ArticleReplyService;
import com.nooblol.global.annotation.UserLoginCheck;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.ResponseUtils;
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
@RequestMapping("/article/reply")
@RequiredArgsConstructor
public class ArticleReplyController {

  private final ArticleReplyService articleReplyService;

  /**
   * 댓글의 단건 조회, 해당 댓글이 없는 경우 NotFound가 Return된다
   *
   * @param replyId
   * @return
   */
  @GetMapping("/{replyId}")
  public ResponseDto getReply(@PathVariable(value = "replyId") int replyId) {
    return ResponseUtils.makeToResponseOkDto(articleReplyService.selectReplyByReplyId(replyId));
  }


  /**
   * Parameter로 받은 Article이 존재하지 않는 경우 BadRequest가 발생한다, 하지만 실제 존재하는 경우 해당 글의 댓글들이 반환되며, 댓글이 존재하지
   * 않으면 null을 받게된다.
   *
   * @param articleId
   * @return
   */
  @GetMapping("/list/{articleId}")
  public ResponseDto getReplyList(@PathVariable(value = "articleId") int articleId) {
    return ResponseUtils.makeResponseOkDtoOfNullable(
        articleReplyService.selectReplyListByArticleId(articleId)
    );
  }


  @UserLoginCheck
  @PostMapping("/")
  public ResponseDto addReply(
      @Valid @RequestBody ReplyInsertDto replyInsertDto, HttpSession session
  ) {
    return ResponseUtils.makeToResponseOkDto(
        articleReplyService.insertReply(replyInsertDto, session)
    );
  }

  @UserLoginCheck
  @PutMapping("/")
  public ResponseDto updateReply(
      @Valid @RequestBody ReplyUpdateDto replyUpdateDto, HttpSession session
  ) {
    return ResponseUtils.makeToResponseOkDto(
        articleReplyService.updateReply(replyUpdateDto, session)
    );
  }

  @UserLoginCheck
  @DeleteMapping("/{replyId}")
  public ResponseDto deleteReply(@PathVariable int replyId, HttpSession session) {
    return ResponseUtils.makeToResponseOkDto(
        articleReplyService.deleteReplyByReplyId(replyId, session)
    );
  }

}
