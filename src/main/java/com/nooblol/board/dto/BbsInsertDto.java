package com.nooblol.board.dto;

import com.nooblol.board.utils.ArticleMessage;
import com.nooblol.board.utils.BoardStatus;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
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
public class BbsInsertDto extends BbsRequestDto {

  @NotNull(message = ArticleMessage.CATEGORY_ID_NULL)
  private Integer categoryId;

  @NotBlank(message = ArticleMessage.BBS_NAME_NULL)
  private String bbsName;

  @NotNull(message = ArticleMessage.BBS_STATUS_NULL)
  private BoardStatus status;

  @Builder
  public BbsInsertDto(String createdUserId, LocalDateTime createdAt, String updatedUserId,
      LocalDateTime updatedAt, Integer categoryId, String bbsName, BoardStatus status) {
    super(createdUserId, createdAt, updatedUserId, updatedAt);
    this.categoryId = categoryId;
    this.bbsName = bbsName;
    this.status = status;
  }
}