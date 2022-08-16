package com.nooblol.account.dto.match;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamDto {

  private String matchId;
  private List<MatchGameBansDto> bans;
}
