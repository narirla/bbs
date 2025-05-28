package com.kh.bbs.web;

import com.kh.bbs.domain.entity.Member;
import com.kh.bbs.svc.member.MemberSVC;
import com.kh.bbs.web.form.member.JoinForm;
import com.kh.bbs.web.form.member.LoginForm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

  private final MemberSVC memberSVC;

  //회원가입 화면
  @GetMapping("/join")
  public String joinForm(Model model){
    model.addAttribute("joinForm", new JoinForm());
    return "member/joinForm";
  }

  //회원가입 처리
  @PostMapping("/join")
  public String join(
      @Valid @ModelAttribute JoinForm joinForm,
      BindingResult bindingResult) {

    // 이메일 중복 체크
    if (memberSVC.existsByEmail(joinForm.getEmail())) {
      bindingResult.rejectValue("email", "duplicate", "이미 사용 중인 이메일입니다.");
    }

    // 비밀번호와 비밀번호 확인이 다를 때
    if (!joinForm.getPassword().equals(joinForm.getConfirmPassword())) {
      bindingResult.rejectValue("confirmPassword", "mismatch", "비밀번호가 일치하지 않습니다.");
    }

    // 유효성 실패 시
    if (bindingResult.hasErrors()) {
      return "member/joinForm";
    }

    // 등록
    Member member = new Member();
    member.setEmail(joinForm.getEmail());
    member.setPassword(joinForm.getPassword());
    member.setNickname(joinForm.getNickname());

    memberSVC.save(member);

    return "redirect:/member/login";
  }

  //로그인 화면
  @GetMapping("/login")
  public String loginForm(Model model) {
    model.addAttribute("loginForm", new LoginForm());
    return "member/loginForm";
  }

  //로그인 처리
  @PostMapping("/login")
  public String login(
      @Valid @ModelAttribute LoginForm loginForm,
      BindingResult bindingResult,
      HttpSession session){
    Optional<Member> optionalMember = memberSVC.findByEmail(loginForm.getEmail());

    if (optionalMember.isEmpty() || !optionalMember.get().getPassword().equals(loginForm.getPassword())){
      bindingResult.reject("loginFail","이메일 또는 비밀번호가 일치하지 않습니다.");
    }

    if (bindingResult.hasErrors()){
      return "member/loginForm";
    }

    //로그인 성공-> 세션 저장
    session.setAttribute("loginMember", optionalMember.get());

    return "redirect:/board/list";
  }

  //로그아웃 처리
  @GetMapping("/logout")
  public String logout(HttpSession session){
    session.invalidate(); // 세션 무효화
    return "redirect:/";
  }





}
