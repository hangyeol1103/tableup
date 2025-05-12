package kr.kh.tableup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import kr.kh.tableup.model.vo.MemberVO;
import kr.kh.tableup.service.MemberService;

@Controller
public class MemberController {

  @Autowired
  MemberService memberService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @GetMapping("/signup")
  public String signupForm(Model model) {
    model.addAttribute("url", "/signup");
    return "member/signup";
  }

  @PostMapping("/signup")
  public String signup(MemberVO member) {
    member.setMe_pw(passwordEncoder.encode(member.getMe_pw()));
    member.setMe_authority("USER");
    memberService.insertMember(member);
    return "redirect:/login";
}

}
