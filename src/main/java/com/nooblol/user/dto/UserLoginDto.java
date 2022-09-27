package com.nooblol.user.dto;

import com.nooblol.user.utils.UserConstants;
import com.nooblol.global.utils.RegexConstants;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

  @NotBlank(message = UserConstants.USER_EMAIL_NULL)
  @Pattern(regexp = RegexConstants.MAIL_REGEX, message = UserConstants.USER_EMAIL_NOT_REGEX)
  private String userEmail;

  @NotBlank(message = UserConstants.USER_PASSWORD_NULL)
  private String userPassword;
}
