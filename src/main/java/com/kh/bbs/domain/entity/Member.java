package com.kh.bbs.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
  private Long id;                // 회원 ID
  private String email;           // 이메일
  private String password;        // 비밀번호
  private String nickname;        // 별칭
  private LocalDateTime createdAt; // 가입일
}
