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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleUpdateRequestDto extends ArticleRequestBaseDto {

  @NotNull(message = ArticleMessage.ARTICLE_ID_NULL)
  private Integer articleId;


  private final LocalDateTime updatedAt = LocalDateTime.now();
}

