package com.nooblol.account.dto.match;

import lombok.Getter;
import lombok.Setter;

/**
 * 매치에 대한 참가자 정보 DTO
 */

@Getter
@Setter
public class MatchGameParticipantsDto {

  private String matchId;

  private String puuid;
  private String summonerId;
  private String summonerLevel;

  private String championName;
  private int championId;
  private int championLevel;

  private String role;
  private String lane;
  private int teamId;
  private String teamPosition;
  private boolean win;

  private int kills;
  private int deaths;
  private int assists;

  private int summoner1Id;
  private int summoner1Casts;
  private int summoner2Id;
  private int summoner2Casts;

  private int item0;
  private int item1;
  private int item2;
  private int item3;
  private int item4;
  private int item5;
  private int item6;

  private MatchGameRunesDto matchGameRunesDto;
}
