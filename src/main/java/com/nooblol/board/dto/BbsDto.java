package com.nooblol.board.dto;

import com.nooblol.board.utils.BoardStatus;
import java.time.LocalDateTime;
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
public class BbsDto {

  private int bbsId;
  private int categoryId;
  private String bbsName;
  private BoardStatus status;
  private String createdUserId;
  private LocalDateTime createdAt;
  private String updatedUserId;
  private LocalDateTime updatedAt;

}
