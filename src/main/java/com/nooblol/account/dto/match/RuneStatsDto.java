package com.nooblol.account.dto.match;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 룬 스탯에 대한 Dto
 */
@Getter
@Setter
@NoArgsConstructor
public class RuneStatsDto {

  private int defense;
  private int flex;
  private int offense;
}
