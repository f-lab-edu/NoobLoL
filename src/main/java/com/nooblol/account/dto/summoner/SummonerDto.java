package com.nooblol.account.dto.summoner;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class SummonerDto {

  private String id;
  private String accountId;
  private String puuid;
  private String name;
  private int profileIconId;
  private long revisionDate;
  private long summonerLevel;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SummonerDto that = (SummonerDto) o;
    return profileIconId == that.profileIconId &&
        revisionDate == that.revisionDate &&
        summonerLevel == that.summonerLevel &&
        Objects.equals(accountId, that.accountId) &&
        Objects.equals(name, that.name) &&
        Objects.equals(id, that.id) &&
        Objects.equals(puuid, that.puuid);
  }

  public boolean equalsMainInfo(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SummonerDto that = (SummonerDto) o;
    return Objects.equals(accountId, that.accountId) &&
        Objects.equals(name, that.name) &&
        Objects.equals(id, that.id) &&
        Objects.equals(puuid, that.puuid);
  }
}