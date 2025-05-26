package com.kh.bbs.web.res;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentRes {
  private Long id;
  private Long boardId;
  private String content;
  private LocalDateTime createdAt;
  private boolean mine; // 현재 로그인 사용자의 댓글 여부
}
