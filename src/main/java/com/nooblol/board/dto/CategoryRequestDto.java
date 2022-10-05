package com.nooblol.board.dto;


import com.nooblol.board.utils.ArticleMessage;
import java.time.LocalDateTime;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 카테고리의 수정 또는 삭제를 하게 될 경우 사용
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String createdUserId;
  private String updatedUserId;



  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CategoryDeleteDto extends CategoryRequestDto {

    private Integer categoryId;
    private Integer status;


    @Builder
    public CategoryDeleteDto(LocalDateTime createdAt, LocalDateTime updatedAt, String createdUserId,
        String updatedUserId, Integer categoryId, Integer status) {
      super(createdAt, updatedAt, createdUserId, updatedUserId);
      this.categoryId = categoryId;
      this.status = status;
    }

  }
}
