package com.nooblol.board.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BbsRequestDto {

  private String createdUserId;

  private LocalDateTime createdAt = LocalDateTime.now();

  private String updatedUserId;

  private LocalDateTime updatedAt = LocalDateTime.now();
}
