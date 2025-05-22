package com.kh.bbs.web.form.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentUpdateForm {

  private Long commentId; // 수정할 댓글의 ID

  @NotBlank(message = "댓글 내용은 필수입니다.")
  private String content; // 수정된 댓글 내용
}
