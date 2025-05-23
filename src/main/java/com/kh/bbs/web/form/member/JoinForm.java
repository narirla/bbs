package com.kh.bbs.web.form.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JoinForm {

  @NotBlank(message = "이메일은 필수입니다.")
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  private String email;

  @NotBlank(message = "비밀번호는 필수입니다.")
  private String password;

  @NotBlank(message = "비밀번호 확인은 필수입니다.")
  private String confirmPassword;

  @NotBlank(message = "닉네임은 필수입니다.")
  private String nickname;
}
