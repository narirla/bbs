package com.kh.bbs.web;

import com.kh.bbs.domain.entity.Board;
import com.kh.bbs.svc.BoardSVC;
import com.kh.bbs.web.form.board.DetailForm;
import com.kh.bbs.web.form.board.SaveForm;
import com.kh.bbs.web.form.board.UpdateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

  private final BoardSVC boardSVC;

  /**
   * 게시글 목록 화면 (/board 또는 /board/list)
   */
  @GetMapping({"", "/list"})
  public String list(Model model) {
    List<Board> boards = boardSVC.findAll();
    model.addAttribute("boards", boards);
    return "board/all"; // ✅ 목록 템플릿: templates/board/all.html
  }

  /**
   * 게시글 작성 화면
   */
  @GetMapping("/create")
  public String createForm(Model model) {
    model.addAttribute("saveForm", new SaveForm());
    return "board/addForm";
  }

  /**
   * 게시글 등록 처리
   */
  @PostMapping("/create")
  public String create(
      @Valid @ModelAttribute("saveForm") SaveForm saveForm,
      BindingResult bindingResult,
      Model model) {

    if (bindingResult.hasErrors()) {
      return "board/addForm";  // 유효성 오류 시 다시 작성 폼
    }

    boardSVC.saveFromForm(saveForm);
    return "redirect:/board";
  }


  /**
   * 게시글 상세 조회 화면
   */
  @GetMapping("/detail/{id}")
  public String detail(@PathVariable Long id, Model model) {
    DetailForm form = new DetailForm();
    form.setId(id);
    model.addAttribute("board", boardSVC.findDetailById(form));
    return "board/detail";
  }

  /**
   * 게시글 수정 화면
   */
  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model) {
    UpdateForm updateForm = boardSVC.getUpdateFormById(id);
    model.addAttribute("updateForm", updateForm);
    return "board/edit";
  }

  /**
   * 게시글 수정 처리
   */
  @PostMapping("/edit")
  public String edit(
      @Valid @ModelAttribute("updateForm") UpdateForm updateForm,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return "board/edit"; // 유효성 실패 시 수정 폼 다시 보여줌
    }

    boardSVC.updateFromForm(updateForm);
    return "redirect:/board";
  }


  /**
   * 게시글 다중 삭제 처리
   */
  @PostMapping("/delete")
  public String delete(@RequestParam("ids") List<Long> ids) {
    boardSVC.deleteBoards(ids);
    return "redirect:/board"; // ✅ 삭제 후 목록으로 이동
  }

  @GetMapping("/delete/{id}")
  public String deleteById(@PathVariable Long id) {
    boardSVC.deleteBoards(List.of(id));
    return "redirect:/board";
  }
}
