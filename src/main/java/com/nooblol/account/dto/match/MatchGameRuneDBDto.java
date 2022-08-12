package com.nooblol.account.dto.match;


import lombok.Getter;
import lombok.Setter;

/**
 * Riot에서 받아온 데이터와 DB의 형식은 다르기 떄문에 따로 Dto추가제작
 */
@Getter
@Setter
public class MatchGameRuneDBDto {

  private String puuid;
  private String matchId;
  private String type;
  private int sortNo;
  private int perk;
}
