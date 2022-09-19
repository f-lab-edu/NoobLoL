package com.nooblol.party.mapper;

import com.nooblol.party.dto.PartyArticleDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/*
명칭을 일차화 하기 위해서 Mapper로 지었을 뿐임.
Redis에 Article에 대한 DAO
 */
@Repository
@RequiredArgsConstructor
public class PartyArticleMapper {

  private final RedisTemplate<String, Object> redisTemplate;

  /**
   * 게시물 추가
   *
   * @param partyArticle
   */
  public void addPartyArticle(PartyArticleDto partyArticle) {

  }

  /**
   * 게시물을 검색, CategoryName과 Id를 통해 Key를 제작하고 일치하는 Article반환
   *
   * @param partyArticle
   * @return
   */
  public PartyArticleDto findPartyArticleByCategoryNameAndId(PartyArticleDto partyArticle) {
    return null;
  }


  /**
   * 조회를 요청한 카테고리의 게시글 종류 모두 반환
   * @param partyArticleDto
   * @return
   */
  public List<PartyArticleDto> findPartyArticleListByCategoryName(PartyArticleDto partyArticleDto) {
    return null;
  }

  /**
   * CategoryName+Id를 통해 Key를 제작함
   *
   * @param categoryName
   * @param id
   * @return
   */
  private String partyKeyGen(String categoryName, int id) {
    return categoryName + "_" + id;
  }
}
