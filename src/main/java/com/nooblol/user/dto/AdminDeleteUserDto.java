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
public class AdminDeleteUserDto extends AdminUserDto {

  @NotBlank(message = "삭제하려는 사용자의 UserId가 입력되지 않았습니다.")
  private String deleteUserId;
}
