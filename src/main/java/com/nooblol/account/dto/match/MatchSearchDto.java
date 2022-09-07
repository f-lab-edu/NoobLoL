package com.nooblol.account.dto.match;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchSearchDto {

  private String puuid;
  private String matchId;
  private int pageNum;
  private int limitNum;

}
