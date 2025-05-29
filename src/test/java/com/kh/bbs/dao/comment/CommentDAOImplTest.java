package com.kh.bbs.dao.comment;

import com.kh.bbs.domain.entity.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentDAOImplTest {

  @Autowired
  CommentDAO commentDAO;

  @Test
  @DisplayName("댓글 등록")
  void save() {
    Comment comment = new Comment();
    comment.setBoardId(1L);           // 존재하는 게시글 ID
    comment.setCommenterId(1L);       // 존재하는 회원 ID
    comment.setContent("테스트 댓글");
    comment.setCreatedAt(LocalDateTime.now());
    comment.setUpdatedAt(LocalDateTime.now());

    Long id = commentDAO.save(comment);
    assertThat(id).isNotNull();
  }

  @Test
  @DisplayName("댓글 50건 등록")
  void save50Comments() {
    for (int i = 1; i <= 50; i++) {
      Comment comment = new Comment();
      comment.setBoardId(1L);           // 테스트용 게시글 ID (사전에 생성 필요)
      comment.setCommenterId(1L);       // 테스트용 회원 ID
      comment.setContent("댓글 내용_" + i);
      comment.setCreatedAt(LocalDateTime.now());
      comment.setUpdatedAt(LocalDateTime.now());

      Long id = commentDAO.save(comment);
      assertThat(id).isNotNull();
    }
  }

  @Test
  @DisplayName("특정 게시글의 전체 댓글 조회")
  void findByBoardId() {
    List<Comment> comments = commentDAO.findByBoardId(1L);
    assertThat(comments).isNotEmpty();
  }

  @Test
  @DisplayName("특정 게시글의 댓글 페이징 조회")
  void findByBoardIdWithPaging() {
    List<Comment> page = commentDAO.findByBoardId(1L, 1, 10);
    assertThat(page).hasSizeLessThanOrEqualTo(10);
  }

  @Test
  @DisplayName("특정 게시글의 총 댓글 수 조회")
  void totalCountByBoardId() {
    int count = commentDAO.totalCountByBoardId(1L);
    assertThat(count).isGreaterThanOrEqualTo(0);
  }

  @Test
  @DisplayName("댓글 단건 조회")
  void findById() {
    // given - 댓글 저장
    Comment comment = new Comment();
    comment.setBoardId(1L);       // 존재하는 게시글 ID
    comment.setCommenterId(1L);   // 존재하는 회원 ID
    comment.setContent("단건 조회용 댓글");
    comment.setCreatedAt(LocalDateTime.now());
    comment.setUpdatedAt(LocalDateTime.now());

    Long id = commentDAO.save(comment);

    // when - 조회
    Optional<Comment> optionalComment = commentDAO.findById(id);

    // then
    assertThat(optionalComment).isPresent();
    assertThat(optionalComment.get().getContent()).isEqualTo("단건 조회용 댓글");
  }


  @Test
  @DisplayName("댓글 수정")
  void update() {
    // 먼저 댓글 저장
    Comment comment = new Comment();
    comment.setBoardId(1L);
    comment.setCommenterId(1L);
    comment.setContent("초기 댓글");
    comment.setCreatedAt(LocalDateTime.now());
    comment.setUpdatedAt(LocalDateTime.now());

    Long id = commentDAO.save(comment);

    // 수정
    String newContent = "수정된 댓글 내용";
    int updated = commentDAO.update(id, newContent);

    // 검증
    assertThat(updated).isEqualTo(1);

    Comment updatedComment = commentDAO.findById(id).orElseThrow();
    assertThat(updatedComment.getContent()).isEqualTo(newContent);
  }


  @Test
  @DisplayName("댓글 삭제")
  void delete() {
    // 먼저 테스트용 댓글 등록
    Comment comment = new Comment();
    comment.setBoardId(1L);
    comment.setCommenterId(1L);
    comment.setContent("삭제 대상 댓글");
    comment.setCreatedAt(LocalDateTime.now());
    comment.setUpdatedAt(LocalDateTime.now());
    Long id = commentDAO.save(comment);

    // 삭제 시도
    int deleted = commentDAO.delete(id);
    assertThat(deleted).isEqualTo(1);
  }

}
