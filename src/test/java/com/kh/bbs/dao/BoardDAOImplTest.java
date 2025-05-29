package com.kh.bbs.dao;

import com.kh.bbs.dao.board.BoardDAO;
import com.kh.bbs.domain.entity.Board;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
class BoardDAOImplTest {

  @Autowired
  BoardDAO boardDAO;

  @Test
  @DisplayName("게시글 등록")
  void save() {
    Board board = new Board();
    board.setTitle("테스트 제목");
    board.setContent("테스트 내용");
    board.setWriter("작성자");

    Long id = boardDAO.save(board);
    log.info("게시글 번호={}", id);
    Assertions.assertThat(id).isNotNull();
  }

  @Test
  @DisplayName("게시글 등록 (100건)")
  void saveBoards() {
    for (int i = 1; i <= 100; i++) {
      Board board = new Board();
      board.setTitle("테스트_" + i);
      board.setContent("테스트 내용_" + i);
      board.setWriter("테스터");
      board.setCreatedAt(new Date());
      board.setUpdatedAt(new Date());

      Long boardId = boardDAO.save(board);
    }
  }



  @Test
  @DisplayName("게시글 목록")
  void findAll() {
    List<Board> list = boardDAO.findAll();
    for (Board board : list) {
      log.info("board={}", board);
    }
    Assertions.assertThat(list).isNotEmpty();
  }

  @Test
  @DisplayName("게시글 단건 조회")
  void findById() {
    Long id = 1L;
    Optional<Board> optionalBoard = boardDAO.findById(id);
    Board board = optionalBoard.orElseThrow();
    log.info("조회된 게시글={}", board);
    Assertions.assertThat(board.getId()).isEqualTo(id);
  }

  @Test
  @DisplayName("게시글 삭제(단건)")
  void deleteById() {
    Long id = 6L;
    int deleted = boardDAO.deleteById(id);
    Assertions.assertThat(deleted).isEqualTo(1);

    Optional<Board> result = boardDAO.findById(id);
    Assertions.assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("게시글 삭제(여러건)")
  void deleteByIds() {
    // 테스트용 데이터 등록
    Board board = new Board();
    board.setTitle("제목1");
    board.setContent("내용1");
    board.setWriter("작성자1");
    Long id1 = boardDAO.save(board);

    // 게시글 2 등록
    Board board2 = new Board();
    board2.setTitle("제목2");
    board2.setContent("내용2");
    board2.setWriter("작성자2");
    Long id2 = boardDAO.save(board2);

    int deleted = boardDAO.deleteByIds(List.of(id1, id2));
    Assertions.assertThat(deleted).isEqualTo(2);
  }

  @Test
  @DisplayName("게시글 수정")
  void updateById() {
    Long id = 1L;
    Board board = new Board();
    board.setTitle("수정된 제목");
    board.setContent("수정된 내용");
    board.setWriter("수정자");

    int updated = boardDAO.updateById(id, board);
    Assertions.assertThat(updated).isEqualTo(1);

    Board updatedBoard = boardDAO.findById(id).orElseThrow();
    Assertions.assertThat(updatedBoard.getTitle()).isEqualTo("수정된 제목");
    Assertions.assertThat(updatedBoard.getWriter()).isEqualTo("수정자");
  }
}