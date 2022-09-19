package com.nooblol.party.service.impl;

import com.nooblol.party.dto.PartyArticleDto;
import com.nooblol.party.service.PartyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {

  private final RedisTemplate redisTemplate;

  @Override
  public PartyArticleDto getPartyArticleByPartyId(int partyId) {
    return null;
  }

  @Override
  public List<PartyArticleDto> getPartyArticleListByCategory(String category) {
    return null;
  }

  @Override
  public boolean updateArticle(PartyArticleDto partyArticleDto) {
    return false;
  }
}
