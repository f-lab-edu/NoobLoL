package com.nooblol.user.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDto {

  @NotBlank(message = "관리자의 UserId가 입력되지 않았습니다.")
  private String adminUserId;
  @NotBlank(message = "관리자의 패스워드가 입력되지 않았습니다.")
  private String adminUserPassword;
}