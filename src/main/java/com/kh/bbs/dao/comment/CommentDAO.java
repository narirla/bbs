package com.kh.bbs.dao.comment;

import com.kh.bbs.domain.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentDAO {

  //댓글 등록
  Long save(Comment comment);

  // 특정 게시글의 댓글 전체 조회
  List<Comment> findByBoardId(Long boardId);

  //댓글 단건 조회
  Optional<Comment> findById(Long id);

  //댓글 수정
  int update(Long id, String content);

  //댓글 삭제
  int delete(Long id);


}
