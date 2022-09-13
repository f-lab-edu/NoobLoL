package com.nooblol.user.dto;

import javax.validation.constraints.AssertTrue;
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
public class LetterDeleteRequestDto {

  @NotNull
  private Integer letterId;

  @NotBlank
  private String type;

  //TODO [22. 09. 14]: 추후 Delete Status에 대한 상수화를 진행하면 초기값 설정
  private int status;

  private String userId;

  //TODO [22. 09. 14]: 추후 Type에 대한 상수화를 진행하면 AssertTrue의 메소드 제작
  @AssertTrue
  public boolean isTypeValidation() {
    return true;
  }
}
