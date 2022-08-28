package com.nooblol.community.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateUserDto extends AdminUserDto {

  @NotBlank(message = "수정하려는 사용자의 UserId가 입력되지 않았습니다.")
  private String updateUserId;

}
