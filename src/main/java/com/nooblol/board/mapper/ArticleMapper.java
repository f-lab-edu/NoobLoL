package com.nooblol.board.mapper;

import com.nooblol.board.dto.ArticleDto;
import com.nooblol.board.dto.ArticleStatusDto;
import com.nooblol.board.dto.LikeAndNotLikeResponseDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper {

  ArticleDto selectArticleByArticleId(int articleId);

  void addReadCount(int articleId);

  int selectUserAuth(String userId);

  int insertArticle(ArticleDto articleDto);

  int updateArticle(ArticleDto articleDto);

  String selectCreatedUserId(int articleId);

  int deleteArticleByArticleId(int articleId);
}
