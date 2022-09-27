package com.nooblol.board.mapper;

import com.nooblol.board.dto.ArticleDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper {

  ArticleDto selectArticleByArticleId(int articleId);

  void addReadCount(int articleId);

  int selectUserAuth(String userId);

  int upsertArticle(ArticleDto articleDto);

  int selectMaxArticleId();

  String selectCreatedUserId(int articleId);
}
