package com.kh.bbs.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Board {
  private Long id;
  private String title;
  private String content;
  private String writer;
  private Date createdAt;
  private Date updatedAt;
}
