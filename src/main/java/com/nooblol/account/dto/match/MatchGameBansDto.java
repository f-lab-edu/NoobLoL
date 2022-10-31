package com.nooblol.account.dto.match;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 매치의 벤에 대한 Dto
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchGameBansDto {

  private int championId;
  private int pickTurn;
}
