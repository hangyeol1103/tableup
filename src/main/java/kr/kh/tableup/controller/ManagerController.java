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

	

	@GetMapping("/main")
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
		List<FoodCategoryVO> foodcategory = managerService.getFoodCategory();
		List<RegionVO> region = managerService.getRegion();
		List<DetailRegionVO> dr = managerService.getDetailRegion();
		List<DetailFoodCategoryVO> dfc = managerService.getDetailFood();
		
		System.out.println(manager.getRm_id());
		System.out.println(restaurant);
		
		model.addAttribute("manager", manager);
		model.addAttribute("restaurant", restaurant);
		
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
	public String menuListPage(Model model, @PathVariable int rt_num,  @AuthenticationPrincipal RestaurantManagerVO manager) {
		System.out.println("menulist rt_num: " + rt_num);
		List<MenuVO> menulist = managerService.getMenuList(rt_num);
		model.addAttribute("menulist", menulist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager);
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

	@PostMapping("/make_menu")
	public String insertMenu(MenuVO menu, MultipartFile mn_img2,  @AuthenticationPrincipal RestaurantManagerVO manager) {
		menu.setMn_rt_num(manager.getRm_rt_num());
		System.out.println(manager);
		System.out.println(menu);
		System.out.println(mn_img2.getOriginalFilename());
		
		int rtNum = manager.getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    menu.setMn_rt_num(rtNum);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }

		if(managerService.makeMenu(menu,mn_img2)){
			return "redirect:/manager/menulist/"+rtNum;
		}

		return "/manager/make_menu/";
	}
	
	
	@GetMapping("/menu/{mn_num}")
	public String detailMenu(Model model, @PathVariable int mn_num) {
		MenuVO menu = managerService.getMenu(mn_num);
		MenuTypeVO menutype =managerService.getMenuType(menu.getMn_mt_num());
		model.addAttribute("menu", menu);
		model.addAttribute("menutype", menutype);
		return "/manager/menu";
	}

	//메뉴 삭제
	@PostMapping("/menu/delete_menu/{mn_num}")
	public String deleteMenuPage(@AuthenticationPrincipal RestaurantManagerVO manager, @PathVariable int mn_num) {
		int rtNum = manager.getRm_rt_num();
		 if(managerService.deleteMenu(mn_num)) {
        return "redirect:/manager/menulist/"+rtNum;
    }
		return "redirect:/manager/menu?mn_num=" + mn_num;
	}
	

	//메뉴 수정 페이지
	@GetMapping("/remake_menu")
	public String reMakeMenuPage(Model model, @AuthenticationPrincipal RestaurantManagerVO manager, @RequestParam("mn_num") int mn_num) {
		List<MenuTypeVO> menutype = managerService.getMenuTypeList();
		MenuVO menu = managerService.getMenu(mn_num);
		System.out.println(menu);

		model.addAttribute("url", "/remake_menu");
		model.addAttribute("menutype", menutype);
		model.addAttribute("menu", menu);
		return "/manager/remake_menu";
	}
	
	@PostMapping("/remake_menu")
	public String updateMenu(MenuVO menu, MultipartFile mn_img2,  @AuthenticationPrincipal RestaurantManagerVO manager) {
		menu.setMn_rt_num(manager.getRm_rt_num());
		System.out.println(manager);
		System.out.println(menu);
		System.out.println(mn_img2.getOriginalFilename());
		
		int rtNum = manager.getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    menu.setMn_rt_num(rtNum);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }

		if(managerService.updateMenu(menu,mn_img2)){
			return "redirect:/manager/menulist/"+rtNum;
		}
		
		return "/manager/remake_menu";
	}
	
	
}
