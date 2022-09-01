package com.nooblol.account.dto.match;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 매치의 벤에 대한 Dto
 */
@Getter
@Setter
@NoArgsConstructor
public class MatchGameBansDto {

  private int championId;
  private int pickTurn;
}
