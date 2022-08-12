package com.nooblol.account.dto.match;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RuneStyleDto {

  private String description;
  private int style;
  private List<RuneStyleSelectionDto> selectionsList;
}
