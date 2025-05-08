package kr.kh.tableup.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import kr.kh.tableup.model.vo.UserVO;
import kr.kh.tableup.service.UserService;

@Controller
public class UserController {

  @Autowired
  UserService userService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @GetMapping("/signup")
  public String signupForm(Model model) {
    model.addAttribute("url", "/signup");
    return "user/signup";
  }

  @PostMapping("/signup")
  public String signup(UserVO user) {
    user.setUs_pw(passwordEncoder.encode(user.getUs_pw()));
    userService.insertUser(user);
    return "redirect:/";
  }

  @GetMapping("/mypage")
  public String mypage(Model model, Principal principal) {
    // principal.getName()은 현재 로그인한 사용자의 username을 반환합니다.
    String username = principal.getName();
    UserVO user = userService.getUserById(username); // 아이디로 회원 정보 조회
    model.addAttribute("user", user);
    model.addAttribute("url", "/mypage");
    return "user/mypage";
}

}
