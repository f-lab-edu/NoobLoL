package com.nooblol.board.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

  private int categoryId;
  private String categoryName;
  private int status;
  private String createdUserId;
  private Timestamp createdAt;
  private String updatedUserId;
  private Timestamp updatedAt;
}
