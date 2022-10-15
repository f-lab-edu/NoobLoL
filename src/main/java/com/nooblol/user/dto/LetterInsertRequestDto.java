package com.nooblol.user.dto;

import com.nooblol.user.utils.LetterConstants;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class LetterInsertRequestDto {

  @NotBlank(message = LetterConstants.LETTER_TITLE_NULL)
  private String letterTitle;

  @NotBlank(message = LetterConstants.LETTER_CONTENT_NULL)
  private String letterContent;

  @NotBlank(message = LetterConstants.LETTER_TO_USER_ID_NULL)
  private String toUserId;
}
