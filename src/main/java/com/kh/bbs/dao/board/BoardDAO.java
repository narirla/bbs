package com.kh.bbs.dao.board;

import com.kh.bbs.domain.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardDAO {

  //게시글 등록
  Long save(Board board);

  //게시글 목록 조회
  List<Board> findAll();

  //게시글 조회
  Optional<Board> findById(Long id);

  //게시글 단건 삭제
  int deleteById(Long id);

  //게시글 여러건 삭제
  int deleteByIds(List<Long> ids);

  //게시글 수정
  int updateById(Long boardId, Board board);
  
}
