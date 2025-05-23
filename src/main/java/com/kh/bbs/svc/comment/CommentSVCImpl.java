package com.kh.bbs.svc.comment;

import com.kh.bbs.dao.comment.CommentDAO;
import com.kh.bbs.domain.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentSVCImpl implements CommentSVC {

  private final CommentDAO commentDAO;

  /**
   * 댓글 등록
   * @param comment 등록할 댓글 정보
   * @return 생성된 댓글 ID
   */
  @Override
  public Long save(Comment comment) {
    return commentDAO.save(comment);
  }

  /**
   * 특정 게시글의 전체 댓글 목록 조회 (페이징 없이 전체)
   * @param boardId 게시글 ID
   * @return 댓글 목록
   */
  @Override
  public List<Comment> findByBoardId(Long boardId) {
    return commentDAO.findByBoardId(boardId);
  }

  /**
   * 특정 게시글의 댓글 목록 조회 (페이징 적용)
   * @param boardId 게시글 ID
   * @param page 현재 페이지 번호 (1부터 시작)
   * @param size 페이지당 댓글 수
   * @return 페이징 처리된 댓글 목록
   */
  @Override
  public List<Comment> findByBoardId(Long boardId, int page, int size) {
    int startRow = (page - 1) * size + 1; // 시작 행 번호 계산
    int endRow = page * size;            // 끝 행 번호 계산
    return commentDAO.findByBoardId(boardId, startRow, endRow);
  }

  /**
   * 댓글 단건 조회
   * @param id 댓글 ID
   * @return 조회된 댓글 (Optional)
   */
  @Override
  public Optional<Comment> findById(Long id) {
    return commentDAO.findById(id);
  }

  /**
   * 댓글 수정
   * @param id 댓글 ID
   * @param content 수정할 내용
   * @return 수정된 행 수
   */
  @Override
  public int update(Long id, String content) {
    return commentDAO.update(id, content);
  }

  /**
   * 댓글 삭제
   * @param id 댓글 ID
   * @return 삭제된 행 수
   */
  @Override
  public int delete(Long id) {
    return commentDAO.delete(id);
  }

  /**
   * 특정 게시글의 총 댓글 수 조회 (페이징 계산용)
   * @param boardId 게시글 ID
   * @return 댓글 총 개수
   */
  @Override
  public int totalCountByBoardId(Long boardId) {
    return commentDAO.totalCountByBoardId(boardId);
  }
}
