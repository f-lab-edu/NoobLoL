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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/article/reply")
@RequiredArgsConstructor
public class ArticleReplyController {

  private final ArticleReplyService articleReplyService;

  @UserLoginCheck
  @PostMapping("/add")
  public ResponseDto addReply(
      @Valid @RequestBody ReplyInsertDto replyInsertDto, HttpSession session
  ) {
    return ResponseUtils.makeToResponseOkDto(
        articleReplyService.insertReply(replyInsertDto, session)
    );
  }

  @UserLoginCheck
  @PostMapping("/update")
  public ResponseDto updateReply(
      @Valid @RequestBody ReplyUpdateDto replyUpdateDto, HttpSession session
  ) {
    return ResponseUtils.makeToResponseOkDto(
        articleReplyService.updateReply(replyUpdateDto, session)
    );
  }

  @UserLoginCheck
  @DeleteMapping("/delete/{replyId}")
  public ResponseDto deleteReply(@PathVariable int replyId, HttpSession session) {
    return ResponseUtils.makeToResponseOkDto(
        articleReplyService.deleteReplyByReplyId(replyId, session)
    );
  }

}
