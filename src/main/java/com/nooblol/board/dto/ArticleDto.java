package com.nooblol.board.dto;

import java.sql.Timestamp;
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

  private int articleId;
  private int bbsId;
  private String articleTitle;
  private int articleReadCount;
  private String articleContent;
  private int status;
  private String createdUserId;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private String authMessage;
}