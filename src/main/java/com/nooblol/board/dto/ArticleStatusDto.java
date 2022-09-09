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
@AllArgsConstructor
@NoArgsConstructor
public class ArticleStatusDto {

  private int articleId;

  private String userId;

  //True : 추천 , False : 비추천
  private boolean type;

  private LocalDateTime createdAt;


  public void setCreatedAtNow() {
    setCreatedAt(LocalDateTime.now());
  }
}
