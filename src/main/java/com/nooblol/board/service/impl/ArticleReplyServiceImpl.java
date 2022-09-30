package com.nooblol.board.service.impl;

import com.nooblol.board.dto.ReplyDto;
import com.nooblol.board.dto.ReplyRequestDto.ReplyInsertDto;
import com.nooblol.board.dto.ReplyRequestDto.ReplyUpdateDto;
import com.nooblol.board.mapper.ArticleReplyMapper;
import com.nooblol.board.service.ArticleReplyService;
import com.nooblol.board.service.ArticleService;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.EncryptUtils;
import com.nooblol.global.utils.SessionUtils;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleReplyServiceImpl implements ArticleReplyService {

  private final ArticleReplyMapper articleReplyMapper;

  private final ArticleService articleService;

  @Override
  public boolean insertReply(ReplyInsertDto insertDto, HttpSession session) {
    validHaveArticleInDb(insertDto.getArticleId());

    Optional<String> userId = Optional.of(SessionUtils.getSessionUserId(session));

    return upsertReply(makeInsertReplyDto(insertDto, userId.get())) > 0;
  }

  @Override
  public boolean updateReply(ReplyUpdateDto updateDto, HttpSession session) {
    validHaveArticleInDb(updateDto.getArticleId());

    boolean isNotCreatedUser = isNotReplyCreatedUser(updateDto.getReplyId(), session);
    boolean isNotUserAdmin = isNotSessionUserAdmin(session);

    if (isNotCreatedUser && isNotUserAdmin) {
      throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
    }

    return upsertReply(makeUpdateReplyDto(updateDto)) > 0;
  }

  @Override
  public int upsertReply(ReplyDto replyDto) {
    return articleReplyMapper.upsertReply(replyDto);
  }

  @Override
  public boolean deleteReplyByReplyId(int replyId, HttpSession session) {
    boolean isNotCreatedUser = isNotReplyCreatedUser(replyId, session);
    boolean isNotUserAdmin = isNotSessionUserAdmin(session);

    if (isNotCreatedUser && isNotUserAdmin) {
      throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
    }

    return articleReplyMapper.deleteReplyByReplyId(replyId) > 0;
  }

  @Override
  public ReplyDto selectReplyByReplyId(int replyId) {
    return articleReplyMapper.selectReplyByReplyId(replyId);
  }

  @Override
  public List<ReplyDto> selectReplyListByArticleId(int articleId) {
    validHaveArticleInDb(articleId);

    return articleReplyMapper.selectReplyListByArticleId(articleId);
  }


  private void validHaveArticleInDb(int articleId) {
    if (articleService.isNotArticleInDb(articleId)) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }
  }

  private boolean isNotReplyCreatedUser(int replyId, HttpSession session) {
    return EncryptUtils.isNotCreatedUser(
        Optional.of(articleReplyMapper.selectCreatedUserIdByReplyId(replyId)).get(),
        Optional.of(SessionUtils.getSessionUserId(session)).get()
    );
  }

  private boolean isNotSessionUserAdmin(HttpSession session) {
    return EncryptUtils.isNotUserAdmin(
        Optional.of(SessionUtils.getSessionUserRole(session)).get()
    );
  }

  private ReplyDto makeInsertReplyDto(ReplyInsertDto insertDto, String userId) {
    return new ReplyDto().builder()
        .replyId(articleReplyMapper.selectMaxReplyId())
        .articleId(insertDto.getArticleId())
        .replyContent(insertDto.getReplyContent())
        .status(insertDto.getStatus())
        .sortNo(articleReplyMapper.selectMaxSortNoByArticleId(insertDto.getArticleId()))
        .createdUserId(userId)
        .createdAt(insertDto.getCreatedAt())
        .build();
  }

  private ReplyDto makeUpdateReplyDto(ReplyUpdateDto updateDto) {
    return new ReplyDto().builder()
        .replyId(updateDto.getReplyId())
        .replyContent(updateDto.getReplyContent())
        .status(updateDto.getStatus())
        .build();
  }
}
