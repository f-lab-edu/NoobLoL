package com.nooblol.board.mapper;

import com.nooblol.board.dto.ArticleStatusDto;
import com.nooblol.board.dto.LikeAndNotLikeResponseDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleStatusMapper {

  ArticleStatusDto selectArticleStatusByArticleIdAndUserId(ArticleStatusDto articleStatusDto);

  LikeAndNotLikeResponseDto selectArticleAllStatusByArticleId(int articleId);

  int insertArticleStatus(ArticleStatusDto articleStatusDto);

  int deleteArticleStatus(ArticleStatusDto articleStatusDto);

}
