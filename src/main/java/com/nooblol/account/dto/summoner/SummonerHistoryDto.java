package com.nooblol.account.dto.summoner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummonerHistoryDto {

  private String leagueId;
  private String summonerId;
  private String summonerName;
  private String queueType;
  private String tier;
  private String rank;
  private int leaguePoints;
  private int wins;
  private int losses;
}
