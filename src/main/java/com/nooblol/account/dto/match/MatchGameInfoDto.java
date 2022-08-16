package com.nooblol.account.dto.match;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 매치 정보에 대한 MasterDto
 */
@Getter
@Setter
@NoArgsConstructor
public class MatchGameInfoDto {

  private String dataVersion;
  private String matchId;

  private long gameCreation;
  private long gameDuration;
  private long gameEndTimestamp;
  private long gameStartTimestamp;
  private long gameId;

  private String gameMode;
  private String gameName;
  private String gameVersion;
  private String platformId;

  private int mapId;
  private int queueId;

  private List<MatchGameParticipantsDto> participants;
  private List<TeamDto> teams;
}
