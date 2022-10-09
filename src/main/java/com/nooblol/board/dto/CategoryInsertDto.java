package com.nooblol.board.dto;

import com.nooblol.board.utils.ArticleMessage;
import com.nooblol.board.utils.CategoryStatus;
import java.time.LocalDateTime;
import javax.validation.constraints.AssertTrue;
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
public class CategoryInsertDto extends CategoryRequestDto {

  @NotBlank(message = ArticleMessage.CATEGORY_NAME_NULL)
  private String categoryName;

  @NotNull(message = ArticleMessage.CATEGORY_STATUS_NULL)
  private CategoryStatus status;

  @Builder
  public CategoryInsertDto(LocalDateTime createdAt, LocalDateTime updatedAt,
      String createdUserId, String updatedUserId, String categoryName, CategoryStatus status) {
    super(createdAt, updatedAt, createdUserId, updatedUserId);
    this.categoryName = categoryName;
    this.status = status;
  }
}
