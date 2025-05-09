package com.kh.bbs.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

  @Autowired
  private BoardService boardService;

  // 게시글 목록
  @GetMapping("/list")
  public String list(Model model) {
    List<Board> boardList = boardService.findAll();
    model.addAttribute("boardList", boardList);
    return "board/list";
  }

  // 게시글 작성 폼
  @GetMapping("/write")
  public String writeForm(Model model) {
    model.addAttribute("board", new Board());
    return "board/write";
  }

  // 게시글 작성 처리
  @PostMapping("/write")
  public String write(@ModelAttribute Board board) {
    boardService.save(board);
    return "redirect:/board/list";
  }

  // 게시글 조회
  @GetMapping("/view/{id}")
  public String view(@PathVariable Long id, Model model) {
    Board board = boardService.findById(id);
    model.addAttribute("board", board);
    return "board/view";
  }

  // 게시글 수정 폼
  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model) {
    Board board = boardService.findById(id);
    model.addAttribute("board", board);
    return "board/edit";
  }

  // 게시글 수정 처리
  @PostMapping("/edit/{id}")
  public String edit(@PathVariable Long id, @ModelAttribute Board board) {
    boardService.update(id, board);
    return "redirect:/board/view/" + id;
  }

  // 게시글 삭제 처리
  @PostMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    boardService.delete(id);
    return "redirect:/board/list";
  }
}
