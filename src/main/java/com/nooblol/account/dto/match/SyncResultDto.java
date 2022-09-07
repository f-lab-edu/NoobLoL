package com.nooblol.account.dto.match;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SyncResultDto {

  private int syncTotalMatch;
  private int syncSuccessMatch;

  public SyncResultDto(int syncTotalMatch, int syncSuccessMatch) {
    this.syncTotalMatch = syncTotalMatch;
    this.syncSuccessMatch = syncSuccessMatch;
  }
}
