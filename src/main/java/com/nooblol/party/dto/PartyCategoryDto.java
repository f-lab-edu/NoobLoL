package com.nooblol.party.dto;

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
public class PartyCategoryDto {

  private int categoryId;
  private String categoryName;
  private int status;
  private String createdUserId;
  private String updatedUserId;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;


}
