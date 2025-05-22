package com.kh.bbs.svc.comment;

import com.kh.bbs.dao.comment.CommentDAO;
import com.kh.bbs.domain.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentSVCImpl implements CommentSVC{

  private final CommentDAO commentDAO;

  /**
   * 댓글 등록
   * @param comment 등록할 댓글
   * @return 생성된 댓글 ID
   */
  @Override
  public Long save(Comment comment) {
    return commentDAO.save(comment);
  }

  /**
   * 댓글 목록 조회
   * @param boardId 게시글 ID
   * @return 댓글 목록
   */
  @Override
  public List<Comment> findByBoardId(Long boardId) {
    return commentDAO.findByBoardId(boardId);
  }

  /**
   * 댓글 단건 조회
   * @param id 댓글 ID
   * @return Optional<Comment>
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
}
