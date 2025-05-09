package com.kh.bbs.svc;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
class BoardServiceImplTest {

  @Autowired
  private BoardRepository boardRepository;

  @Autowired
  private BoardService boardService;

  @Test
  @DisplayName("게시글 작성")
  void createBoard() {
    Board board = new Board();
    board.setTitle("테스트 제목");
    board.setContent("테스트 내용");
    board.setAuthor("테스트 작성자");

    // 게시글 작성
    boardService.createBoard(board);

    // 게시글이 저장되었는지 확인
    Optional<Board> savedBoard = boardRepository.findById(board.getId());
    Assertions.assertThat(savedBoard).isPresent();
    Assertions.assertThat(savedBoard.get().getTitle()).isEqualTo("테스트 제목");
  }

  @Test
  @DisplayName("게시글 조회")
  void getBoard() {
    Long boardId = 1L; // 실제 데이터베이스에 있는 게시글 ID로 수정해야 함
    Optional<Board> board = boardRepository.findById(boardId);

    board.ifPresent(b -> log.info("조회된 게시글={}", b));

    board.ifPresentOrElse(
        b -> Assertions.assertThat(b.getTitle()).isEqualTo("테스트 제목"),
        () -> Assertions.fail("게시글을 찾을 수 없습니다.")
    );
  }

  @Test
  @DisplayName("게시글 수정")
  void updateBoard() {
    Long boardId = 1L; // 실제 데이터베이스에 있는 게시글 ID로 수정해야 함
    Board board = boardRepository.findById(boardId).orElseThrow();
    board.setTitle("수정된 제목");
    board.setContent("수정된 내용");

    boardService.updateBoard(boardId, board);

    Optional<Board> updatedBoard = boardRepository.findById(boardId);
    Assertions.assertThat(updatedBoard).isPresent();
    Assertions.assertThat(updatedBoard.get().getTitle()).isEqualTo("수정된 제목");
  }

  @Test
  @DisplayName("게시글 삭제")
  void deleteBoard() {
    Long boardId = 1L; // 실제 데이터베이스에 있는 게시글 ID로 수정해야 함

    boardService.deleteBoard(boardId);

    Optional<Board> deletedBoard = boardRepository.findById(boardId);
    Assertions.assertThat(deletedBoard).isEmpty();
  }

  @Test
  @DisplayName("게시글 목록 조회")
  void getAllBoards() {
    List<Board> boards = boardService.getAllBoards();

    boards.forEach(board -> log.info("게시글 목록={}", board));

    Assertions.assertThat(boards).isNotEmpty();
  }

  @Test
  @DisplayName("게시글 삭제 후 확인")
  void deleteBoardWithConfirmation() {
    Long boardId = 2L; // 실제 데이터베이스에 있는 게시글 ID로 수정해야 함

    // 게시글 삭제
    boardService.deleteBoard(boardId);

    // 삭제 후 게시글 확인
    Optional<Board> board = boardRepository.findById(boardId);
    Assertions.assertThat(board).isEmpty();
  }
}
