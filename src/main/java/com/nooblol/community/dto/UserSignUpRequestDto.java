package com.nooblol.community.dto;

import com.nooblol.community.utils.UserRoleStatus;
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

  @NotBlank(message = "이메일이 입력되지 않았습니다.")
  @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
  private String userEmail;

  @NotBlank(message = "이름이 입력되지 않았습니다.")
  private String userName;

  @NotBlank(message = "패스워드가 입력되지 않았습니다.")
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
