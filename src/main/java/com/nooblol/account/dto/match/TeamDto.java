package com.nooblol.account.dto.match;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDto {

  private List<MatchGameBansDto> teamBanList;
}
