package com.nooblol.party.service;


import com.nooblol.party.dto.PartyArticleDto;
import java.util.List;

public interface PartyService {

  PartyArticleDto getPartyArticleByPartyId(int partyId);

  List<PartyArticleDto> getPartyArticleListByCategory(String category);

  boolean updateArticle(PartyArticleDto partyArticleDto);
}
