package com.nooblol.community.dto;

import com.nooblol.community.utils.UserConstants;
import com.nooblol.community.utils.UserRoleStatus;
import com.nooblol.global.utils.RegexConstants;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDto {

  private String userId = UUID.randomUUID().toString();

  @NotBlank(message = UserConstants.USER_EMAIL_NULL)
  @Pattern(regexp = RegexConstants.MAIL_REGEX, message = UserConstants.USER_EMAIL_NOT_REGEX)
  private String userEmail;

  @NotBlank(message = UserConstants.USER_NAME_NULL)
  private String userName;

  @NotBlank(message = UserConstants.USER_PASSWORD_NULL)
  private String password;

  private int userRole = UserRoleStatus.UNAUTH_USER.getRoleValue();

  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime updatedAt = LocalDateTime.now();

  public void setAdminUserRole() {
    setUserRole(UserRoleStatus.ADMIN.getRoleValue());
  }

  @Builder
  public UserSignUpRequestDto(String userEmail, String userName, String password) {
    this.userEmail = userEmail;
    this.userName = userName;
    this.password = password;
  }
}
