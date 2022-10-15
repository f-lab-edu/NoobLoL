package com.nooblol.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nooblol.user.utils.LetterStatus;
import com.nooblol.user.utils.LetterType;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LetterDto {

  private int letterId;

  private String letterTitle;

  private String letterContent;

  private String toUserId;

  private LetterStatus toStatus;

  private String fromUserId;

  private LetterStatus fromStatus;

  private LocalDateTime createdAt;

  private LetterType type;
}
