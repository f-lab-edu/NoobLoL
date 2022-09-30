package com.nooblol.board.dto;

import com.nooblol.board.utils.ArticleMessage;
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
public class ReplyRequestDto {

  @NotNull(message = ArticleMessage.ARTICLE_ID_NULL)
  private Integer articleId;

  @NotNull(message = ArticleMessage.REPLY_CONTENT_NULL)
  private String replyContent;

  //1 = 활성화, 9 = 삭제
  @NotNull(message = ArticleMessage.REPLY_STATUS_NULL)
  private Integer status;

  private Integer sortNo;

}
