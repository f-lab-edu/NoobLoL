package com.nooblol.board.dto;

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
public class ArticleInsertRequestDto extends ArticleRequestBaseDto {

  private String createdUserId;
  private int articleReadCount;
  private final LocalDateTime createdAt = LocalDateTime.now();
  private final LocalDateTime updatedAt = LocalDateTime.now();
}

