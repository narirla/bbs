package com.kh.bbs.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
  private Long id;                 // 댓글 ID
  private Long boardId;           // 게시글 ID (FK)
  private Long commenterId;       // 회원 ID (FK)
  private String content;         // 댓글 내용
  private LocalDateTime createdAt; // 작성일
  private LocalDateTime updatedAt; // 수정일
}
