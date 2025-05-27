package com.kh.bbs.web;

import com.kh.bbs.domain.entity.Comment;
import com.kh.bbs.domain.entity.Member;
import com.kh.bbs.svc.comment.CommentSVC;
import com.kh.bbs.web.res.CommentRes;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentRestController {

  private final CommentSVC commentSVC;

  // 댓글 등록
  @PostMapping
  public ResponseEntity<Long> save(@RequestBody Comment comment, HttpSession session){
    Object loginMember = session.getAttribute("loginMember");
    if (loginMember == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
    }

    // 작성자 ID 설정
    comment.setCommenterId(((Member) loginMember).getId());

    Long generatedId = commentSVC.save(comment);
    return ResponseEntity.ok(generatedId);
  }

  // 댓글 목록 조회 (mine 포함)
  @GetMapping("/board/{boardId}")
  public ResponseEntity<List<CommentRes>> findByBoardId(@PathVariable Long boardId, HttpSession session){
    // loginMemberId를 final로 유지
    final Long loginMemberId;

    Object loginMember = session.getAttribute("loginMember");
    if (loginMember != null && loginMember instanceof Member) {
      loginMemberId = ((Member) loginMember).getId();
    } else {
      loginMemberId = null;
    }

    List<Comment> comments = commentSVC.findByBoardId(boardId);

    List<CommentRes> response = comments.stream()
        .map(c -> {
          CommentRes dto = new CommentRes();
          dto.setId(c.getId());
          dto.setBoardId(c.getBoardId());
          dto.setContent(c.getContent());
          dto.setCreatedAt(c.getCreatedAt());

          boolean mine = false;
          if (loginMemberId != null && c.getCommenterId() != null) {
            mine = loginMemberId.equals(c.getCommenterId());
          }
          dto.setMine(mine);
          dto.setNickname(c.getNickname());

          return dto;
        })
        .toList();

    return ResponseEntity.ok(response);
  }


  // 댓글 페이징 목록 (기존 그대로 유지)
  @GetMapping("/board/{boardId}/pages")
  public ResponseEntity<?> findByBoardIdPaged(
      @PathVariable Long boardId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      HttpSession session
  ) {
    List<Comment> comments = commentSVC.findByBoardId(boardId, page, size);
    int totalCount = commentSVC.totalCountByBoardId(boardId);
    int totalPages = (int) Math.ceil((double) totalCount / size);

    // ✅ 로그인 사용자 ID 명확히 가져오기 (Java 8~17 안전버전)
    Long loginMemberIdTemp = null;
    Object loginMember = session.getAttribute("loginMember");
    if (loginMember != null && loginMember instanceof Member) {
      loginMemberIdTemp = ((Member) loginMember).getId();
    }
    final Long loginMemberId = loginMemberIdTemp;

    List<CommentRes> response = comments.stream()
        .map(c -> {
          CommentRes dto = new CommentRes();
          dto.setId(c.getId());
          dto.setBoardId(c.getBoardId());
          dto.setContent(c.getContent());
          dto.setCreatedAt(c.getCreatedAt());
          dto.setNickname(c.getNickname());
          dto.setMine(loginMemberId != null && loginMemberId.equals(c.getCommenterId()));
          return dto;
        })
        .toList();

    return ResponseEntity.ok(
        Map.of(
            "comments", response,
            "page", page,
            "totalPages", totalPages
        )
    );
  }


  // 댓글 단건 조회
  @GetMapping("/{id}")
  public ResponseEntity<CommentRes> findById(@PathVariable Long id, HttpSession session) {
    Optional<Comment> optionalComment = commentSVC.findById(id);
    if (optionalComment.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Comment comment = optionalComment.get();
    Long sessionLoginId = null;
    Object loginMember = session.getAttribute("loginMember");
    if (loginMember instanceof Member member) {
      sessionLoginId = member.getId();
    }

    CommentRes res = new CommentRes();
    res.setId(comment.getId());
    res.setBoardId(comment.getBoardId());
    res.setContent(comment.getContent());
    res.setCreatedAt(comment.getCreatedAt());
    res.setMine(sessionLoginId != null && sessionLoginId.equals(comment.getCommenterId()));
    res.setNickname(comment.getNickname()); // ✍ 작성자 닉네임 포함

    return ResponseEntity.ok(res);
  }


  // 댓글 수정
  @PutMapping("/{id}")
  public ResponseEntity<Integer> update(
      @PathVariable Long id,
      @RequestBody Comment comment,
      HttpSession session
  ) {
    Object loginMember = session.getAttribute("loginMember");
    if (loginMember == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<Comment> optionalComment = commentSVC.findById(id);
    if (optionalComment.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Comment savedComment = optionalComment.get();
    Long loginMemberId = ((Member) loginMember).getId();

    // ✅ null-safe 비교
    if (savedComment.getCommenterId() == null || !loginMemberId.equals(savedComment.getCommenterId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    int updateRow = commentSVC.update(id, comment.getContent());
    return ResponseEntity.ok(updateRow);
  }


  // 댓글 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Integer> delete(@PathVariable Long id, HttpSession session) {
    Object loginMember = session.getAttribute("loginMember");
    if (loginMember == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<Comment> optionalComment = commentSVC.findById(id);
    if (optionalComment.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Comment savedComment = optionalComment.get();
    Long loginMemberId = ((Member) loginMember).getId();

    // ✅ null-safe 비교
    if (savedComment.getCommenterId() == null || !loginMemberId.equals(savedComment.getCommenterId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    int deleteRow = commentSVC.delete(id);
    return ResponseEntity.ok(deleteRow);
  }

}
