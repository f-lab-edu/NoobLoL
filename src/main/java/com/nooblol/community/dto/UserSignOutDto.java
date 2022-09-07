package com.nooblol.community.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignOutDto {

  @NotBlank(message = "UserId가 입력되지 않았습니다.")
  private String userId;

  @NotBlank(message = "패스워드가 입력되지 않았습니다.")
  private String password;

}
