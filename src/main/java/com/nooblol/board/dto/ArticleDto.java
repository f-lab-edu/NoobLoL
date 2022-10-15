package com.nooblol.board.dto;

import com.nooblol.board.utils.ArticleStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDto {

  /*
  Integer로 변경하는 이유는 기본값 0이 들어감으로 인해서 Mybatis에서 NullCheck를 못하는 경우를 제외하기 위해 변경
   */

  private Integer articleId;
  private Integer bbsId;
  private String articleTitle;
  private int articleReadCount;
  private String articleContent;
  private ArticleStatus status;
  private String createdUserId;
  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime updatedAt = LocalDateTime.now();
  private String authMessage;
}
