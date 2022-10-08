package com.nooblol.board.dto;

import com.nooblol.board.utils.ArticleMessage;
import com.nooblol.board.utils.ReplyStatus;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyInsertDto extends ReplyRequestDto {

  private LocalDateTime createdAt = LocalDateTime.now();

  @Builder
  public ReplyInsertDto(
      @NotNull(message = ArticleMessage.ARTICLE_ID_NULL) Integer articleId,
      @NotNull(message = ArticleMessage.REPLY_CONTENT_NULL) String replyContent,
      @NotNull(message = ArticleMessage.REPLY_STATUS_NULL) ReplyStatus status,
      Integer sortNo
  ) {
    super(articleId, replyContent, status, sortNo);
  }
}