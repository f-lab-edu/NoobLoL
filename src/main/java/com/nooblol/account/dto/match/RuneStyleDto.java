package com.nooblol.account.dto.match;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RuneStyleDto {

  private String description;
  private int style;
  private List<RuneStyleSelectionDto> selections;
}
