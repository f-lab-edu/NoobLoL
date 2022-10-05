package com.nooblol.board.dto;

import com.nooblol.board.utils.ArticleMessage;
import com.nooblol.board.utils.ReplyStatusEnum;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRequestDto {

  @NotNull(message = ArticleMessage.ARTICLE_ID_NULL)
  private Integer articleId;

  @NotNull(message = ArticleMessage.REPLY_CONTENT_NULL)
  private String replyContent;

  @NotNull(message = ArticleMessage.REPLY_STATUS_NULL)
  private ReplyStatusEnum status;

  private Integer sortNo;

}
