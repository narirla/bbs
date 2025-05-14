package com.kh.bbs.svc;

import com.kh.bbs.domain.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardSVC {

  //등록
  Long save(Board board);

  //게시글 목록
  List<Board> findAll();

  // 게시글 단건 조회
  Optional<Board> findById(Long id);

  // 게시글 수정
  int update(Long id, Board board);

  // 게시글 삭제
  int deleteId(Long id);

  // 여러 게시글 삭제
  int deleteIds(List<Long> ids);
}
