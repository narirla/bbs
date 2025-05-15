package com.kh.bbs.web.form.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateForm {

  private Long id;

  @NotBlank(message = "제목은 필수입니다.")
  @Size(max = 100, message = "제목은 100자 이하로 입력해주세요.")
  private String title;

  @NotBlank(message = "내용은 필수입니다.")
  private String content;

  @NotBlank(message = "작성자는 필수입니다.")
  @Size(max = 20, message = "작성자는 20자 이하로 입력해주세요.")
  private String writer;

  private Date updatedDate;
}
