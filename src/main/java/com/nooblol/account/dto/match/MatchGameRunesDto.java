package com.nooblol.account.dto.match;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 사용자의 룬정보에 대한 DTO
 */

@Getter
@Setter
@NoArgsConstructor
public class MatchGameRunesDto {

  private RuneStatsDto statPerks;
  private List<RuneStyleDto> styles;
}
