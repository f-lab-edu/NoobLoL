package com.nooblol.board.dto;


import com.nooblol.board.utils.ArticleMessage;
import com.nooblol.board.utils.ArticleStatusEnum;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequestBaseDto {

  @NotNull(message = ArticleMessage.BBS_ID_NULL)
  private Integer bbsId;

  @NotBlank(message = ArticleMessage.ARTICLE_TITLE_NULL)
  private String articleTitle;

  @NotBlank(message = ArticleMessage.ARTICLE_CONTENT_NULL)
  private String articleContent;

  @NotNull(message = ArticleMessage.ARTICLE_STATUS_NULL)
  private ArticleStatusEnum status;
}
