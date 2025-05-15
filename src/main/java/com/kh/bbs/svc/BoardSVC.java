package com.kh.bbs.svc;

import com.kh.bbs.domain.entity.Board;
import com.kh.bbs.web.form.board.SaveForm;
import com.kh.bbs.web.form.board.DetailForm;
import com.kh.bbs.web.form.board.UpdateForm;

import java.util.List;
import java.util.Optional;

public interface BoardSVC {

  // ========== [엔티티 기반] ==========

  /**
   * 게시글 등록
   * @param board Board 엔티티 객체
   * @return 생성된 게시글 ID
   */
  Long save(Board board);

  /**
   * 게시글 전체 목록 조회
   * @return 게시글 목록
   */
  List<Board> findAll();

  /**
   * 게시글 단건 조회
   * @param id 게시글 ID
   * @return Optional<Board>
   */
  Optional<Board> findById(Long id);

  /**
   * 게시글 수정
   * @param id 게시글 ID
   * @param board 수정할 게시글 데이터
   * @return 수정된 행 수
   */
  int update(Long id, Board board);

  /**
   * 게시글 삭제 (단건)
   * @param id 게시글 ID
   * @return 삭제된 행 수
   */
  int deleteId(Long id);

  /**
   * 게시글 삭제 (여러 건)
   * @param ids 삭제할 게시글 ID 목록
   * @return 삭제된 행 수
   */
  int deleteIds(List<Long> ids);


  // ========== [폼 기반 - SaveForm, DetailForm, UpdateForm] ==========

  /**
   * SaveForm 기반 게시글 저장
   * @param saveForm 사용자 입력 등록 폼
   * @return 생성된 게시글 ID
   */
  Long saveFromForm(SaveForm saveForm);

  /**
   * DetailForm 기반 게시글 조회
   * @param detailForm ID 포함한 폼 객체
   * @return 조회된 게시글 (Board 또는 DTO)
   */
  Board findDetailById(DetailForm detailForm);

  /**
   * 게시글 수정 폼 조회
   * @param id 수정할 게시글 ID
   * @return UpdateForm
   */
  UpdateForm getUpdateFormById(Long id);

  /**
   * UpdateForm 기반 게시글 수정 처리
   * @param updateForm 사용자 입력 수정 폼
   */
  void updateFromForm(UpdateForm updateForm);

  /**
   * 게시글 여러 건 삭제 - Controller 전용 서비스 메서드
   * @param ids 삭제할 게시글 ID 목록
   */
  void deleteBoards(List<Long> ids);
}
