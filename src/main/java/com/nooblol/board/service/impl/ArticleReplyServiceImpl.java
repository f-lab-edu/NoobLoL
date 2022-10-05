package com.nooblol.board.service.impl;

import com.nooblol.board.dto.ReplyDto;
import com.nooblol.board.dto.ReplyInsertDto;
import com.nooblol.board.dto.ReplyUpdateDto;
import com.nooblol.board.mapper.ArticleReplyMapper;
import com.nooblol.board.service.ArticleReplyService;
import com.nooblol.board.service.ArticleService;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionUtils;
import com.nooblol.user.utils.UserRoleStatus;
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
    articleService.checkNotExistsArticleByArticleId(insertDto.getArticleId());

    Optional<String> userId = Optional.of(SessionUtils.getSessionUserId(session));

    return upsertReply(makeInsertReplyDto(insertDto, userId.get())) > 0;
  }

  @Override
  public boolean updateReply(ReplyUpdateDto updateDto, HttpSession session) {
    articleService.checkNotExistsArticleByArticleId(updateDto.getArticleId());

    if (isNotReplyCreatedUser(updateDto.getReplyId(), session) &&
        UserRoleStatus.isNotUserAdmin(SessionUtils.getSessionUserRole(session))) {
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
    if (isNotReplyCreatedUser(replyId, session) &&
        UserRoleStatus.isNotUserAdmin(SessionUtils.getSessionUserRole(session))) {
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
    articleService.checkNotExistsArticleByArticleId(articleId);

    return articleReplyMapper.selectReplyListByArticleId(articleId);
  }

  private boolean isNotReplyCreatedUser(int replyId, HttpSession session) {
    Optional<String> createdUserIdOptional =
        Optional.of(articleReplyMapper.selectCreatedUserIdByReplyId(replyId));
    return !createdUserIdOptional.get().equals(SessionUtils.getSessionUserId(session));
  }

  private ReplyDto makeInsertReplyDto(ReplyInsertDto insertDto, String userId) {
    return new ReplyDto().builder()
        .articleId(insertDto.getArticleId())
        .replyContent(insertDto.getReplyContent())
        .status(insertDto.getStatus())
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
