package com.kh.bbs.web.form.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class SaveForm {

  @NotBlank(message = "제목은 필수입니다.")
  @Size(min = 1, max = 100, message = "제목은 100자 이하로 입력해주세요.")
  private String title;       // 게시글 제목

  @NotBlank(message = "내용은 필수입니다.")
  private String content;     // 게시글 내용

  @NotBlank(message = "작성자는 필수입니다.")
  @Size(min = 2, max = 20, message = "작성자는 2자 이상 20자 이하로 입력해주세요.")
  private String writer;      // 작성자

  private Date createdDate;   // 생성일 (서버에서 자동 처리)
  private Date updatedDate;   // 수정일 (서버에서 자동 처리)
}
