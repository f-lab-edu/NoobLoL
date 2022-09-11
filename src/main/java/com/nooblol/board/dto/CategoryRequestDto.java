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
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

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
  public static class CategoryInsertDto extends CategoryRequestDto {

    @NotBlank(message = ArticleMessage.CATEGORY_NAME_NULL)
    private String categoryName;

    @NotNull(message = ArticleMessage.CATEGORY_STATUS_NULL)
    private Integer status;


    @Builder
    public CategoryInsertDto(LocalDateTime createdAt, LocalDateTime updatedAt,
        String createdUserId, String updatedUserId, String categoryName, Integer status) {
      super(createdAt, updatedAt, createdUserId, updatedUserId);
      this.categoryName = categoryName;
      this.status = status;
    }
  }


  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CategoryUpdateDto extends CategoryRequestDto {

    @NotNull(message = ArticleMessage.CATEGORY_ID_NULL)
    private Integer categoryId;

    private String newCategoryName;
    private Integer status;

    @AssertTrue(message = ArticleMessage.CATEGORY_UPDATE_VALIDATION)
    public boolean isNewCategoryInfoValid() {
      if (StringUtils.isBlank(newCategoryName) && ObjectUtils.isEmpty(status)) {
        return false;
      }
      return true;
    }

    @Builder
    public CategoryUpdateDto(LocalDateTime createdAt, LocalDateTime updatedAt, String createdUserId,
        String updatedUserId, Integer categoryId, String newCategoryName, Integer status) {
      super(createdAt, updatedAt, createdUserId, updatedUserId);
      this.categoryId = categoryId;
      this.newCategoryName = newCategoryName;
      this.status = status;
    }
  }

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
