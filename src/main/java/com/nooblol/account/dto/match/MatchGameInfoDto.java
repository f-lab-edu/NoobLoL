package com.nooblol.account.dto.match;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 매치 정보에 대한 MasterDto
 */
@Getter
@Setter
public class MatchGameInfoDto {

  private String dataVersion;
  private String matchId;

  private long gameCreation;
  private long gameDuration;
  private long gameEndTimeStamp;
  private long gameStartTimeStamp;
  private long gameId;

  private String gameMode;
  private String gameName;
  private String gameVersion;
  private String platformId;

  private int mapId;
  private int queueId;

  private List<MatchGameParticipantsDto> participantsList;
}
