package com.nooblol.account.dto.match;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchUseRuneDto {

  private String puuid;
  private String matchId;
  private String type;
  private int sortNo;
  private int perk;
}
