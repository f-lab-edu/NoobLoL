package com.nooblol.community.dto;

import com.nooblol.community.utils.AdminConstants;
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

  @NotBlank(message = AdminConstants.ADMIN_USERID_NULL)
  private String adminUserId;

  @NotBlank(message = AdminConstants.ADMIN_PASSWORD_NULL)
  private String adminUserPassword;
}