package com.nooblol.account.dto.match;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//간단한 전적 반환
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchGameSimpleDto {

  private String puuid;
  private String matchId;

  private String summonerId;
  private String summonerName;
  private String championName;
  private int championId;

  private String role;
  private String lane;
  private int teamId;
  private String teamPosition;
  private boolean win;

  private int kills;
  private int deaths;
  private int assists;

  private int summoner1Id;
  private int summoner2Id;

  private int item0;
  private int item1;
  private int item2;
  private int item3;
  private int item4;
  private int item5;
  private int item6;

  private long gameCreation;
  private long gameDuration;


  private int queueId;
  private String gameMode;
  private List<MatchGameSimpleDto> participants;
}
