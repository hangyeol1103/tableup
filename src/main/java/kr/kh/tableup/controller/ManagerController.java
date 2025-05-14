package kr.kh.tableup.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import kr.kh.tableup.model.util.CustomUser;
import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.MenuTypeVO;
import kr.kh.tableup.model.vo.MenuVO;
import kr.kh.tableup.model.vo.RegionVO;
import kr.kh.tableup.model.vo.RestaurantManagerVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.service.ManagerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
		int rm_num=manager.getRm_num();
		RestaurantVO restaurant =managerService.selectRestaurant(rm_num);
		//해당 레스토랑의 기본키를 가진 사진들만 가져오기 위한 작업
		List<FileVO> fileList = managerService.getFileListByRtNum(restaurant.getRt_num());
		restaurant.setFileList(fileList);
		System.out.println(fileList);
		//지역,음식 카테고리에 집어넣기위해 해당 테이블 값들을 가져옴
		List<FoodCategoryVO> foodcategory = managerService.getFoodCategory();
		List<RegionVO> region = managerService.getRegion();
		List<DetailRegionVO> dr = managerService.getDetailRegion();
		List<DetailFoodCategoryVO> dfc = managerService.getDetailFood();
		
		System.out.println(manager.getRm_id());
		System.out.println(restaurant);
		
		
		model.addAttribute("manager", manager);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("fileList", fileList);
		
		model.addAttribute("foodcategory", foodcategory);
		model.addAttribute("region", region);
		model.addAttribute("dr", dr);
		model.addAttribute("dfc", dfc);
		
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
		List<FoodCategoryVO> foodcategory = managerService.getFoodCategory();
		List<RegionVO> region = managerService.getRegion();
		List<DetailRegionVO> dr = managerService.getDetailRegion();
		List<DetailFoodCategoryVO> dfc = managerService.getDetailFood();

		System.out.println(region);
		System.out.println(dr);
		System.out.println(foodcategory);
		System.out.println(dfc);

		model.addAttribute("url", "/make");
		model.addAttribute("foodcategory", foodcategory);
		model.addAttribute("region", region);
		model.addAttribute("dr", dr);
		model.addAttribute("dfc", dfc);
		return "/manager/make";
	}

	@ResponseBody
	@GetMapping("/get-detail")
	public List<DetailFoodCategoryVO> getDetailCategory(@RequestParam("fcNum") int fc_num) {
    System.out.println("받은 fcNum : " + fc_num);
		return managerService.getDetailByFcNum(fc_num);
	}

	@ResponseBody
	@GetMapping("/get-detailregion")
	public List<DetailRegionVO> getDetailRegion(@RequestParam("regNum") int reg_num) {
    System.out.println("받은 regNum : " + reg_num);
		return managerService.getDetailByRegNum(reg_num);
	}
	
	@PostMapping("/make")
	public String insertPage(RestaurantVO restaurant,@RequestParam("fileList") MultipartFile[] fileList,  
													 @AuthenticationPrincipal RestaurantManagerVO manager ) {
		System.out.println(manager);
		System.out.println(restaurant);

		if(managerService.insertRestaurant(restaurant, manager, fileList)){
			return "redirect:/manager/restaurant";
		}
		return "redirect:/manager/make";
	}
	
	//메뉴 리스트 출력
	@GetMapping("/menulist/{rt_num}")
	public String menuListPage(Model model, @PathVariable int rt_num) {
		System.out.println("menulist rt_num: " + rt_num);
		List<MenuVO> menulist = managerService.getMenuList(rt_num);
		model.addAttribute("menulist", menulist);
		return "manager/menulist";
	}

	//메뉴 등록 페이지
	@GetMapping("/make_menu")
	public String makeMenuPage(Model model) {
		List<MenuTypeVO> menutype = managerService.getMenuTypeList();
		
		model.addAttribute("url", "/make_menu");
		model.addAttribute("menutype", menutype);
		return "/manager/make_menu";
	}
	
	
}
