package com.kh.bbs.svc.board;

import com.kh.bbs.dao.board.BoardDAO;
import com.kh.bbs.domain.entity.Board;
import com.kh.bbs.web.form.board.DetailForm;
import com.kh.bbs.web.form.board.SaveForm;
import com.kh.bbs.web.form.board.UpdateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardSVCImpl implements BoardSVC {

  private final BoardDAO boardDAO;

  // 게시글 등록 - 엔티티 직접
  @Override
  public Long save(Board board) {
    return boardDAO.save(board);
  }

  // 게시글 등록 - SaveForm 기반
  @Override
  public Long saveFromForm(SaveForm form) {
    Board board = new Board();
    board.setTitle(form.getTitle());
    board.setContent(form.getContent());
    board.setWriter(form.getWriter());           // SaveForm의 author → Board의 writer
    board.setCreatedAt(new Date());
    board.setUpdatedAt(new Date());

    return boardDAO.save(board);
  }

  // 게시글 목록
  @Override
  public List<Board> findAll() {
    return boardDAO.findAll();
  }

  // 게시글 단건 조회 - Optional 반환
  @Override
  public Optional<Board> findById(Long id) {
    return boardDAO.findById(id);
  }

  // 게시글 상세 조회 - DetailForm 기반
  @Override
  public Board findDetailById(DetailForm form) {
    return boardDAO.findById(form.getId())
        .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
  }

  // 게시글 수정 - 엔티티 기반
  @Override
  public int update(Long id, Board board) {
    return boardDAO.updateById(id, board);
  }

  // 게시글 수정 - UpdateForm 기반
  @Override
  public void updateFromForm(UpdateForm form) {
    Board board = new Board();
    board.setTitle(form.getTitle());
    board.setContent(form.getContent());
    board.setWriter(form.getWriter());
    board.setUpdatedAt(new Date());

    boardDAO.updateById(form.getId(), board);
  }

  // 수정폼 데이터 조회
  @Override
  public UpdateForm getUpdateFormById(Long id) {
    Board board = boardDAO.findById(id)
        .orElseThrow(() -> new RuntimeException("수정할 게시글이 없습니다."));

    UpdateForm form = new UpdateForm();
    form.setId(board.getId());
    form.setTitle(board.getTitle());
    form.setContent(board.getContent());
    form.setWriter(board.getWriter());  // writer 사용
    form.setUpdatedDate(board.getUpdatedAt());

    return form;
  }

  // 게시글 단건 삭제
  @Override
  public int deleteId(Long id) {
    return boardDAO.deleteById(id);
  }

  // 게시글 여러 건 삭제
  @Override
  public int deleteIds(List<Long> ids) {
    return boardDAO.deleteByIds(ids);
  }

  // Controller에서 호출하는 다중 삭제용 편의 메서드
  @Override
  public void deleteBoards(List<Long> ids) {
    boardDAO.deleteByIds(ids);
  }
}
