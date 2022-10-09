package com.nooblol.board.dto;

import com.nooblol.board.utils.ArticleLikeStatus;
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

  private ArticleLikeStatus likeType;

  private LocalDateTime createdAt = LocalDateTime.now();
}
