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

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ReplyInsertDto extends ReplyRequestDto {

    private LocalDateTime createdAt = LocalDateTime.now();
  }


  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ReplyUpdateDto extends ReplyRequestDto {

    /*
    댓글의 Update의 경우에는 Created와 sortNo에 대해서는 정보수정을 진행하지 않는다.
    또한 Update일자나 마지막 User에 대한 정보역시 따로 남기지 않기 때문에 업데이트가 가능하도록 replyId만 받는다.
     */
    @NotNull(message = ArticleMessage.REPLY_ID_NULL)
    private Integer replyId;
  }
}
