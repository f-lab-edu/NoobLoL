package com.nooblol.board.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {

  private Integer replyId;

  private int articleId;

  private String replyContent;

  private int status;

  private int sortNo;

  private String createdUserId;

  private LocalDateTime createdAt;

}
