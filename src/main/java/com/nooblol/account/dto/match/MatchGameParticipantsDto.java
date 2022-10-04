package com.nooblol.account.dto.match;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 매치에 대한 참가자 정보 DTO
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchGameParticipantsDto {

  private String puuid;

  private String summonerId;
  private String summonerLevel;
  private String summonerName;
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

  private MatchGameRunesDto perks;
}
