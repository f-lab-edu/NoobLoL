package com.nooblol.board.dto;


import com.nooblol.board.utils.ArticleMessage;
import com.nooblol.board.utils.BoardStatus;
import java.time.LocalDateTime;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BbsUpdateDto extends BbsRequestDto {

  @NotNull(message = ArticleMessage.BBS_ID_NULL)
  private Integer bbsId;

  private Integer categoryId;

  private String bbsName;

  private BoardStatus status;

  @AssertTrue(message = ArticleMessage.BBS_UPDATE_VALIDATION)
  public boolean isValidUpdateData() {
    if (ObjectUtils.isEmpty(categoryId) &&
        StringUtils.isBlank(bbsName) &&
        ObjectUtils.isEmpty(status)) {
      return false;
    }

    return true;
  }


  @Builder
  public BbsUpdateDto(String createdUserId, LocalDateTime createdAt, String updatedUserId,
      LocalDateTime updatedAt, Integer bbsId, Integer categoryId, String bbsName,
      BoardStatus status) {
    super(createdUserId, createdAt, updatedUserId, updatedAt);
    this.bbsId = bbsId;
    this.categoryId = categoryId;
    this.bbsName = bbsName;
    this.status = status;
  }
}
