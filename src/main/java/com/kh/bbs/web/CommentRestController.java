package com.kh.bbs.web;

import com.kh.bbs.domain.entity.Comment;
import com.kh.bbs.svc.comment.CommentSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentRestController {

  private final CommentSVC commentSVC;

  //댓글 등록
  @PostMapping
  public ResponseEntity<Long> save(@RequestBody Comment comment){
    Long generatedId = commentSVC.save(comment);
    return ResponseEntity.ok(generatedId);
  }

  //댓글 목록 조회
  @GetMapping("/board/{boardId}")
  public ResponseEntity<List<Comment>> findByBoardId(@PathVariable Long boardId){
    List<Comment> comments = commentSVC.findByBoardId(boardId);
    return ResponseEntity.ok(comments);
  }

  //댓글 단건 조회
  @GetMapping("/{id}")
  public ResponseEntity<Comment> findById(@PathVariable Long id){
    Optional<Comment> optionalComment = commentSVC.findById(id);
    return optionalComment.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
  }


  //댓글 수정
  @PutMapping("/{id}")
  public ResponseEntity<Integer> update(
      @PathVariable Long id,
      @RequestBody Comment comment
  ){
    int updateRow = commentSVC.update(id, comment.getContent());
    return ResponseEntity.ok(updateRow);
  }


  //댓글 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Integer> delete(@PathVariable Long id){
    int deleteRow = commentSVC.delete(id);
    return ResponseEntity.ok(deleteRow);
  }


}
