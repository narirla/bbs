package com.kh.bbs.svc;

import com.kh.bbs.dao.BoardDAO;
import com.kh.bbs.domain.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardSVCImpl implements BoardSVC {

  final private BoardDAO boardDAO;

  // 게시글 등록
  @Override
  public Long save(Board board) {
    return boardDAO.save(board);
  }

  // 게시글 목록 조회
  @Override
  public List<Board> findAll() {
    return boardDAO.findAll();
  }

  // 게시글 단건 조회
  @Override
  public Optional<Board> findById(Long id) {
    return boardDAO.findById(id);
  }

  // 게시글 수정
  @Override
  public int update(Long id, Board board) {
    return boardDAO.updateById(id, board);
  }

  // 게시글 단건 삭제
  @Override
  public int deleteId(Long id) {
    return boardDAO.deleteById(id);
  }

  // 게시글 여러건 삭제
  @Override
  public int deleteIds(List<Long> ids) {
    return boardDAO.deleteByIds(ids);
  }
}
