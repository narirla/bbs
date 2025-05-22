package com.kh.bbs.web.form.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JoinForm {

  @NotBlank(message = "이메일은 필수입니다.")
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  private String email;           // 이메일

  @NotBlank(message = "비밀번호는 필수입니다.")
  @Size(min = 6, max = 20, message = "비밀번호는 6~20자 사이로 입력해주세요.")
  private String password;        // 비밀번호

  @NotBlank(message = "별칭은 필수입니다.")
  @Size(min = 2, max = 10, message = "별칭은 2~10자 사이로 입력해주새요.")
  private String nickname;        // 별칭
}
