package kr.kh.tableup.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.kh.tableup.model.vo.UserVO;
import kr.kh.tableup.service.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /** 로그인*/
    @GetMapping("/login")
    public String login(HttpSession session) {
      session.removeAttribute("loginError");
      session.removeAttribute("loginId");
      return "user/login";
    }


    /** 회원가입*/
    @GetMapping("/signup")
    public String signupForm() {
      return "user/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute UserVO user, RedirectAttributes ra) {
      boolean result = userService.registerUser(user);
      ra.addFlashAttribute("msg", result ? "회원가입이 완료되었습니다." : "회원가입에 실패했습니다.");
      return "redirect:/user/login";
    }

    /** 마이페이지 */
    @GetMapping("/mypage")
    public String mypage(Model model, Principal principal) {
        if (principal == null) return "user/mypage/indexnot";

        UserVO user = userService.getUserById(principal.getName());
        model.addAttribute("user", user);
        return "user/mypage/index";
    }

    @GetMapping("/edit")
    public String editForm(Model model, Principal principal) {
        if (principal == null) return "redirect:/user/login";

        UserVO user = userService.getUserById(principal.getName());
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute UserVO user, Principal principal, RedirectAttributes ra) {
        if (principal == null) return "redirect:/user/login";

        user.setUs_id(principal.getName()); // ID 보안
        boolean result = userService.updateUser(user);

        ra.addFlashAttribute("msg", result ? "회원정보가 수정되었습니다." : "수정에 실패했습니다.");
        return "redirect:/user/mypage";
    }

    /** 중복 검사 */
    @GetMapping("/check-duplicate")
    @ResponseBody
    public Map<String, Boolean> checkDuplicate(
            @RequestParam String type,
            @RequestParam String value) {
        return Map.of("duplicate", userService.isDuplicate(type, value));
    }
}
