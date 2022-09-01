package com.nooblol.board.dto;

import java.sql.Timestamp;
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
  private int status;
  private String createdUserId;
  private Timestamp createdAt;
  private String updatedUserId;
  private Timestamp updatedAt;

}
