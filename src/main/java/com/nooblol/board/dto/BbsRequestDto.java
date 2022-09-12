package com.nooblol.board.dto;

import com.nooblol.board.utils.ArticleMessage;
import com.nooblol.board.utils.BoardStatusEnum;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BbsRequestDto {

  private String createdUserId;

  private LocalDateTime createdAt;

  private String updatedUserId;

  private LocalDateTime updatedAt;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BbsInsertDto extends BbsRequestDto {

    @NotNull(message = ArticleMessage.CATEGORY_ID_NULL)
    private Integer categoryId;

    @NotBlank(message = ArticleMessage.BBS_NAME_NULL)
    private String bbsName;

    @NotNull(message = ArticleMessage.BBS_STATUS_NULL)
    private Integer status;

    @Builder
    public BbsInsertDto(String createdUserId, LocalDateTime createdAt, String updatedUserId,
        LocalDateTime updatedAt, Integer categoryId, String bbsName, Integer status) {
      super(createdUserId, createdAt, updatedUserId, updatedAt);
      this.categoryId = categoryId;
      this.bbsName = bbsName;
      this.status = status;
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BbsUpdateDto extends BbsRequestDto {

    @NotNull(message = ArticleMessage.BBS_ID_NULL)
    private Integer bbsId;

    private Integer categoryId;

    private String bbsName;

    private Integer status;

    /*
    TODO [22. 09 .12] : assertTrue가 정상적으로 사용이 되지않는 듯함.
     */
    @AssertTrue
    public boolean validationUpdateData() {
      System.out.println("????????");
      /*if (ObjectUtils.isEmpty(categoryId) &&
          StringUtils.isBlank(bbsName) &&
          ObjectUtils.isEmpty(status)) {
        return false;
      }*/

      if (categoryId == 0 && bbsName == null && status == 0) {
        return true;
      }
      return false;
    }


    @Builder
    public BbsUpdateDto(String createdUserId, LocalDateTime createdAt, String updatedUserId,
        LocalDateTime updatedAt, Integer bbsId, Integer categoryId, String bbsName,
        Integer status) {
      super(createdUserId, createdAt, updatedUserId, updatedAt);
      this.bbsId = bbsId;
      this.categoryId = categoryId;
      this.bbsName = bbsName;
      this.status = status;
    }
  }

  @Getter
  @Setter
  public static class BbsDeleteDto extends BbsRequestDto {

    int bbsId;

    int status;

    public BbsDeleteDto(int bbsId, String userId) {
      this.bbsId = bbsId;
      this.status = BoardStatusEnum.DELETE.getStatus();
      setUpdatedUserId(userId);
      setUpdatedAt(LocalDateTime.now());
    }
  }

}
