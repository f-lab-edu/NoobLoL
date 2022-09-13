package com.nooblol.user.dto;

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

  @NotBlank
  private String letterTitle;

  @NotBlank
  private String letterContent;

  @NotBlank
  private String toUserId;

  @NotBlank
  private String fromUserId;

  @NotNull
  private Integer toStatus;

  @NotNull
  private Integer fromStatus;

  @NotNull
  private Integer toReadStatus;

  private LocalDateTime createdAt;
}
