package com.kh.bbs.web;

import com.kh.bbs.domain.entity.Board;
import com.kh.bbs.svc.BoardSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

  //BoardSVC 주입
  final private BoardSVC boardSVC;

  // 게시글 목록 화면
  @GetMapping("/list")
  public String list(Model model) {
    List<Board> boards = boardSVC.findAll();  // 게시글 목록 조회
    model.addAttribute("boards", boards);  // 모델에 게시글 목록 추가
    return "board/list";  // 목록 페이지로 이동
  }

  // 게시글 작성 화면
  @GetMapping("/addform")
  public String createForm(Model model) {
    model.addAttribute("board", new Board());  // 새로운 Board 객체 추가
    return "board/addform";  // 게시글 작성 화면으로 이동
  }

  // 게시글 등록 처리
  @PostMapping("/create")
  public String create(@ModelAttribute Board board) {
    boardSVC.save(board);  // 게시글 등록
    return "redirect:/board/list";  // 목록 페이지로 리다이렉트
  }

  // 게시글 조회 화면
  @GetMapping("/{id}")
  public String view(@PathVariable Long id, Model model) {
    Optional<Board> board = boardSVC.findById(id);  // 게시글 조회
    if (board.isPresent()) {
      model.addAttribute("board", board.get());  // 조회된 게시글을 모델에 추가
      return "board/view";  // 게시글 조회 화면으로 이동
    } else {
      return "redirect:/board/list";  // 게시글이 없으면 목록 페이지로 리다이렉트
    }
  }

  // 게시글 수정 화면
  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model) {
    Optional<Board> board = boardSVC.findById(id);  // 게시글 조회
    if (board.isPresent()) {
      model.addAttribute("board", board.get());  // 게시글을 모델에 추가
      return "board/edit";  // 게시글 수정 화면으로 이동
    } else {
      return "redirect:/board/list";  // 게시글이 없으면 목록 페이지로 리다이렉트
    }
  }

  // 게시글 수정 처리
  @PostMapping("/edit/{id}")
  public String edit(@PathVariable Long id, @ModelAttribute Board board) {
    boardSVC.update(id, board);  // 게시글 수정
    return "redirect:/board/" + id;  // 수정된 게시글 상세 페이지로 리다이렉트
  }

  // 게시글 삭제(단건)
  @PostMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    boardSVC.deleteId(id);  // 게시글 삭제
    return "redirect:/board/list";  // 목록 페이지로 리다이렉트
  }

  // 게시글 삭제(여러건)
  @PostMapping("/delete/ids")
  public String deleteMultiple(@RequestParam List<Long> ids) {
    boardSVC.deleteIds(ids);  // 여러 게시글 삭제
    return "redirect:/board/list";  // 목록 페이지로 리다이렉트
  }

}
