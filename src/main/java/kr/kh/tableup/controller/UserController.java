package kr.kh.tableup.controller;

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

}
