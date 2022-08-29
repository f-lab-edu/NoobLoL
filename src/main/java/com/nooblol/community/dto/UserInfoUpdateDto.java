package com.nooblol.community.dto;


import com.nooblol.community.utils.UserRoleStatus;
import java.sql.Timestamp;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * newUserName,  newPassword의 경우 빈값이 들어와도 상관이 없다.
 */
@Getter
@Setter
public class UserInfoUpdateDto {

  @NotBlank(message = "UserId가 입력되지 않았습니다.")
  private String userId;

  @NotBlank(message = "이메일이 입력되지 않았습니다.")
  @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
  private String userEmail;


  @NotBlank
  @NotBlank(message = "이름이 입력되지 않았습니다.")
  private String orgUserName;

  private String newUserName;

  @NotBlank(message = "패스워드가 입력되지 않았습니다.")
  private String orgPassword;

  private String newPassword;

  //회원에 대해서 최소 1부터 시작함. 0은 GUEST라 상관이 없슴
  @Min(value = 1, message = "게스트는 수정가능한 정보가 없습니다.")
  private int userRole;

  private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

  @AssertTrue(message = "정보 수정이 불가능한 계정입니다")
  public boolean isReqUserInfoUpdateRoleValidation() {
    if (getUserRole() == UserRoleStatus.AUTH_USER.getRoleValue() ||
        getUserRole() == UserRoleStatus.ADMIN.getRoleValue()) {
      return true;
    }
    return false;
  }
}
