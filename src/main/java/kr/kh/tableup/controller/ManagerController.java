package kr.kh.tableup.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import kr.kh.tableup.model.util.CustomUser;
import kr.kh.tableup.model.vo.RestaurantManagerVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.service.ManagerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestBody;







@Controller
@RequestMapping("/manager")
public class ManagerController {
	
	@Autowired
	ManagerService managerService;

	@Autowired
  PasswordEncoder passwordEncoder;

	@GetMapping("main")
	public String manager(Model model) {
		model.addAttribute("url","/main");
		return "manager/main";
	}
	

	@GetMapping("/login")
	public String manager_login(Model model) {
		model.addAttribute("url", "/login");
		return "manager/login";
	}


	@GetMapping("/signup")
	public String manager_signup(Model model) {
		model.addAttribute("url", "/signup");
		return "/manager/signup";
	}
	
	@PostMapping("/signup")
	public String postMethodName(RestaurantManagerVO rm) {
		rm.setRm_pw(passwordEncoder.encode(rm.getRm_pw()));
		managerService.insertManager(rm);
		return "redirect:/manager/login";
	}
	
	@GetMapping("/restaurant/{rm_id}")
	public String restaurantPage(@PathVariable("rm_id") String rm_id,Model model, Principal principal) {
		String loginId = principal.getName();
		
		if (!loginId.equals(rm_id)) {
			return "redirect:/manager/main";
	}
		RestaurantManagerVO manager = managerService.getManagerId(loginId);
		//해당 매니저의 매장 외래키를 가져옴
		int rm_num=manager.getRm_no();
		RestaurantVO restaurant =managerService.selectRestaurant(rm_num);
		
		System.out.println(manager);
		System.out.println(restaurant);
		
		model.addAttribute("manager", manager);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("url", "/restaurant");
		return "/manager/restaurant";
	}

	@GetMapping("/restaurant")
    public String redirectRestaurant(Principal principal) {
        String loginId = principal.getName();
        return "redirect:/manager/restaurant/" + loginId;
    }

	@GetMapping("/make")
	public String makePage(Model model) {
		model.addAttribute("url", "/make");
		return "/manager/make";
	}

	@PostMapping("/make")
	public String insertPage(RestaurantVO restaurant, MultipartFile file,  @AuthenticationPrincipal RestaurantManagerVO manager) {
		System.out.println(manager);
		System.out.println(restaurant);

		if(managerService.insertRestaurant(restaurant, manager, file)){
			return "redirect:/manager/restaurant";
		}
		return "redirect:/manager/make";
	}
	
	
	
}
