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

    return articleReplyMapper.insertReply(
        new ReplyDto().builder()
            .articleId(insertDto.getArticleId())
            .replyContent(insertDto.getReplyContent())
            .status(insertDto.getStatus())
            .createdUserId(Optional.of(SessionUtils.getSessionUserId(session)).get())
            .createdAt(insertDto.getCreatedAt())
            .build()) > 0;
  }

  @Override
  public boolean updateReply(ReplyUpdateDto updateDto, HttpSession session) {
    articleService.checkNotExistsArticleByArticleId(updateDto.getArticleId());

    if (isReplyCreatedUserOrAdminUser(updateDto.getReplyId(), session)) {
      return articleReplyMapper.updateReply(
          new ReplyDto().builder()
              .replyId(updateDto.getReplyId())
              .replyContent(updateDto.getReplyContent())
              .status(updateDto.getStatus())
              .build()
      ) > 0;
    }
    throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
  }

  @Override
  public boolean deleteReplyByReplyId(int replyId, HttpSession session) {
    if (isReplyCreatedUserOrAdminUser(replyId, session)) {
      return articleReplyMapper.deleteReplyByReplyId(replyId) > 0;
    }

    throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
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

  private boolean isReplyCreatedUserOrAdminUser(int replyId, HttpSession session) {
    Optional<String> createdUserIdOptional =
        Optional.of(articleReplyMapper.selectCreatedUserIdByReplyId(replyId));
    return UserRoleStatus.isUserRoleAdmin(SessionUtils.getSessionUserRole(session)) ||
        createdUserIdOptional.get().equals(SessionUtils.getSessionUserId(session));
  }

}
