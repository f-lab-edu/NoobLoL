package com.nooblol.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LetterSearchDto {

  private String userId;

  private int[] statusArr;

  private int pageNum;

  private int limitNum;

  private String letterType;
}
