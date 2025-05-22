package com.kh.bbs.svc.comment;

import com.kh.bbs.domain.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentSVC {

  /**
   * 댓글 등록
   * @param comment 등록할 댓글
   * @return 생성된 댓글 ID
   */
  Long save(Comment comment);

  /**
   * 특정 게시글의 댓글 목록 조회
   * @param boardId 게시글 ID
   * @return 댓글 목록
   */
  List<Comment> findByBoardId(Long boardId);

  /**
   * 댓글 단건 조회
   * @param id 댓글 ID
   * @return Optional<Comment>
   */
  Optional<Comment> findById(Long id);

  /**
   * 댓글 수정
   * @param id 댓글 ID
   * @param content 수정할 내용
   * @return 수정된 행 수
   */
  int update(Long id, String content);

  /**
   * 댓글 삭제
   * @param id 댓글 ID
   * @return 삭제된 행 수
   */
  int delete(Long id);

}
