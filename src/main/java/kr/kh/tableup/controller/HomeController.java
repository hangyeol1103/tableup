package kr.kh.tableup.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.kh.tableup.model.vo.UserVO;
import kr.kh.tableup.service.UserService;



@Controller
public class HomeController {
	
	@Autowired
	UserService userService;

	@GetMapping("/")
	public String home(Model model) {

		UserVO user = userService.getUserById("123");
		System.out.println(user);

		model.addAttribute("sample", user.getUs_name());
		model.addAttribute("url", "/");
		return "index";
	}

	
}
