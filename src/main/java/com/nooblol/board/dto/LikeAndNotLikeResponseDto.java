package com.nooblol.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeAndNotLikeResponseDto {

  private int likeCnt;
  private int notLikeCnt;

}
