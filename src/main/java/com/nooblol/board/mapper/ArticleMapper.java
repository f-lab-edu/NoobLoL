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

  int upsertArticle(ArticleDto articleDto);

  int selectMaxArticleId();

  String selectCreatedUserId(int articleId);

  int deleteArticleByArticleId(int articleId);

  /**
   * 특정 사용자가 해당글에 추천, 비추천 를 하였는지 확인하기 위함.
   *
   * @param articleStatusDto
   * @return
   */
  ArticleStatusDto selectArticleStatusByArticleIdAndUserId(ArticleStatusDto articleStatusDto);

  /**
   * 파라미터로 제공한 게시물의 총 추천, 비추천 수를 Return한다
   *
   * @param articleId
   * @return
   */
  LikeAndNotLikeResponseDto selectArticleAllStatusByArticleId(int articleId);

  int insertArticleStatus(ArticleStatusDto articleStatusDto);

  /**
   * 게시물의 추천, 비추천 에 대한 데이터를 삭제한다.
   * <p>
   * int의 Default값은 0이 들어가나 articleId의 경우에는 1부터 사용하기 떄문에 ArticleId를 설정안하면 아무 데이터도 삭제가 되지 않기 떄문에 문제가
   * 없다.
   *
   * @param articleStatusDto
   * @return
   */
  int deleteArticleStatue(ArticleStatusDto articleStatusDto);

}
