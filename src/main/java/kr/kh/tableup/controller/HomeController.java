package kr.kh.tableup.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.kh.tableup.service.UserService;



@Controller
public class HomeController {
	
	@Autowired
	UserService userService;

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("url", "/login");
		return "user/login";
	}

	@GetMapping("/signup")
  public String signupForm(Model model) {
    model.addAttribute("url", "/signup");
    return "user/signup";
  }
	


	  // 중복 검사
  @GetMapping("/check-duplicate")
  @ResponseBody
  public Map<String, Boolean> checkDuplicate(
    @RequestParam String type,
    @RequestParam String value) {
    
    Map<String, Boolean> response = new HashMap<>();
    boolean isDuplicate = false;
    
    switch(type) {
      case "id":
        isDuplicate = userService.isIdDuplicate(value);
        break;
      case "phone":
        isDuplicate = userService.isPhoneDuplicate(value);
        break;
      case "email":
        isDuplicate = userService.isEmailDuplicate(value);
        break;
    }
    
    response.put("duplicate", isDuplicate);
    return response;
  }
	
	
}
