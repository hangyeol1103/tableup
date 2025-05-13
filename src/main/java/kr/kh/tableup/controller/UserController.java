package kr.kh.tableup.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.kh.tableup.model.vo.UserVO;
import kr.kh.tableup.service.UserService;


@Controller
@RequestMapping("user")
public class UserController {

  @Autowired
  UserService userService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @PostMapping("/signup")
  public String signup(UserVO user) {
    user.setUs_pw(passwordEncoder.encode(user.getUs_pw()));
    userService.insertUser(user);
    return "redirect:/";
  }

  // 마이페이지
  @GetMapping("/mypage")
  public String mypage(Model model, Principal principal) {
    String username = principal.getName();
    UserVO user = userService.getUserById(username);
    model.addAttribute("user", user);
    model.addAttribute("url", "/mypage");
    return "user/mypage";
  }

  // 마이페이지 수정
  @GetMapping("/edit")
  public String editForm(Model model, Principal principal) {
    String username = principal.getName();
    UserVO user = userService.getUserById(username);
    model.addAttribute("user", user);
    model.addAttribute("url", "/mypage");
    return "user/edit";
  }

  // 수정 처리
  @PostMapping("/edit")
  public String edit(UserVO user, Principal principal, RedirectAttributes ra) {
    String username = principal.getName();
    user.setUs_id(username); // 아이디는 로그인 정보로 고정
    boolean result = userService.updateUserInfo(user);
    if(result) {
        ra.addFlashAttribute("msg", "회원정보가 수정되었습니다.");
    } else {
        ra.addFlashAttribute("msg", "수정에 실패했습니다.");
    }
    return "redirect:/mypage";
  }



}
