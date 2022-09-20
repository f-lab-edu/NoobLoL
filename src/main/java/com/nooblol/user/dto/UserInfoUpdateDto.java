package com.nooblol.user.dto;


import com.nooblol.user.utils.UserRoleStatus;
import com.nooblol.user.utils.UserConstants;
import com.nooblol.global.utils.RegexConstants;
import java.sql.Timestamp;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/*
 * newUserName,  newPassword의 경우 빈값이 들어와도 상관이 없다.
 */
@Getter
@Setter
public class UserInfoUpdateDto {

  @NotBlank(message = UserConstants.USER_ID_NULL)
  private String userId;

  @NotBlank(message = UserConstants.USER_EMAIL_NULL)
  @Pattern(regexp = RegexConstants.MAIL_REGEX, message = UserConstants.USER_EMAIL_NOT_REGEX)
  private String userEmail;


  @NotBlank(message = UserConstants.USER_NAME_NULL)
  private String orgUserName;

  private String newUserName;

  @NotBlank(message = UserConstants.USER_PASSWORD_NULL)
  private String orgPassword;

  private String newPassword;

  //회원에 대해서 최소 1부터 시작함. 0은 GUEST라 상관이 없슴
  @Min(value = 1, message = UserConstants.USER_ROLE_INFO_UPDATE_CANT_GUEST)
  private int userRole;

  private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

  @AssertTrue(message = UserConstants.USER_ROLE_INFO_UPDATE_CANT_ACCOUNT)
  public boolean isReqUserInfoUpdateRoleValidation() {
    if (getUserRole() == UserRoleStatus.AUTH_USER.getRoleValue() ||
        getUserRole() == UserRoleStatus.ADMIN.getRoleValue()) {
      return true;
    }
    return false;
  }
}
