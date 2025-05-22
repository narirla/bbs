package com.kh.bbs.web.form.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentSaveForm {

  @NotBlank(message = "댓글 내용은 필수입니다.")
  private String content;     // 댓글 내용

  private Long boardId;       // 어느 게시글에 대한 댓글인지
}
