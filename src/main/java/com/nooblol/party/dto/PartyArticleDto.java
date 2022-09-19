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
public class PartyArticleDto {

  private Long id;
  private int categoryId;
  private String categoryName;
  private String title;
  private String content;
  private int articleStatus;
  private String writerCharacter;
  private String articlePasswordHash;
  private LocalDateTime createdAt;

}
