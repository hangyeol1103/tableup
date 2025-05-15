package kr.kh.tableup.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import kr.kh.tableup.model.util.CustomUser;
import kr.kh.tableup.model.vo.BusinessDateVO;
import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.MenuTypeVO;
import kr.kh.tableup.model.vo.MenuVO;
import kr.kh.tableup.model.vo.RegionVO;
import kr.kh.tableup.model.vo.ResCouponVO;
import kr.kh.tableup.model.vo.ResNewsVO;
import kr.kh.tableup.model.vo.RestaurantDetailVO;
import kr.kh.tableup.model.vo.RestaurantManagerVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.service.ManagerService;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
	
	//메뉴 상세 페이지
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
	
	//매장 상세 정보 출력
	@GetMapping("/restaurantdetail/{rt_num}")
	public String restaurantDetailPage(Model model, @PathVariable int rt_num, @AuthenticationPrincipal RestaurantManagerVO manager) {
		RestaurantDetailVO resdetail = managerService.getResDetail(rt_num);

		model.addAttribute("manager", manager);
		model.addAttribute("resdetail", resdetail);
		return "/manager/restaurantdetail";
	}

	@GetMapping("/make_detail")
	public String makeResDetailPage(Model model) {
		model.addAttribute("url", "/make_detail");
		return "/manager/make_detail";
	}
	
	@PostMapping("/make_detail")
	public String insertResDetailPage(RestaurantDetailVO resdetail, @AuthenticationPrincipal RestaurantManagerVO manager ) {
		int rtNum = manager.getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    resdetail.setRd_rt_num(rtNum);
		
		System.out.println(manager);
		System.out.println(resdetail);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }
		if(managerService.insertResDetail(resdetail)){
			return "redirect:/manager/restaurantdetail/"+ rtNum;
		}
		return "redirect:/manager/make_detail";
	}

	//상세정보 변경 페이지
	@GetMapping("/remake_detail")
	public String reMakeDetailPage(Model model, @AuthenticationPrincipal RestaurantManagerVO manager) {

		model.addAttribute("url", "/remake_detail");
		return "/manager/remake_detail";
	}
	
	@PostMapping("/remake_detail")
	public String updateDetail(RestaurantDetailVO resdetail, @AuthenticationPrincipal RestaurantManagerVO manager ) {
		int rtNum = manager.getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    resdetail.setRd_rt_num(rtNum);
		
		System.out.println(manager);
		System.out.println(resdetail);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }

		if(managerService.updateDetail(resdetail)){
			return "redirect:/manager/restaurantdetail/"+ rtNum;
		}
		
		return "/manager/remake_detail";
	}

	//매니저 쿠폰 페이지
	
	//쿠폰 리스트
	@GetMapping("/couponlist/{rt_num}")
	public String couponListPage(Model model, @PathVariable int rt_num,  @AuthenticationPrincipal RestaurantManagerVO manager) {
		System.out.println("couponlist rt_num: " + rt_num);
		List<ResCouponVO> couponlist = managerService.getCouponList(rt_num);
		
		model.addAttribute("couponlist", couponlist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager);
		return "manager/couponlist";
	}
	//매장 소식 페이지
	//매장 소식 리스트
	@GetMapping("/newslist/{rt_num}")
	public String newsListPage(Model model, @PathVariable int rt_num,  @AuthenticationPrincipal RestaurantManagerVO manager) {
		System.out.println("newslist rt_num: " + rt_num);
		List<ResNewsVO> newslist = managerService.getNewsList(rt_num);

		model.addAttribute("newslist", newslist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager);
		return "manager/newslist";
	}
	//영업 시간 페이지
	@GetMapping("/opertimelist/{rt_num}")
	public String OperTimePage(Model model, @PathVariable int rt_num,  @AuthenticationPrincipal RestaurantManagerVO manager) {
		System.out.println("opertimelist rt_num: " + rt_num);
		List<BusinessDateVO> opertimelist = managerService.getOperTimeList(rt_num);

		model.addAttribute("opertimelist", opertimelist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager);
		return "manager/opertimelist";
	}

	//예약 가능 시간 페이지
	//예약 가능 시간 목록(리스트)
	@GetMapping("/restimelist/{rt_num}")
	public String ResTimeListPage(Model model, @PathVariable int rt_num,  @AuthenticationPrincipal RestaurantManagerVO manager) {
		System.out.println("restimelist rt_num: " + rt_num);
		List<BusinessHourVO> restimelist = managerService.getResTimeList(rt_num);

		model.addAttribute("restimelist", restimelist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager);
		return "manager/restimelist";
	}

	//예약 시간 등록 페이지
	@GetMapping("/make_restime")
	public String makeResTimePage(Model model) {
		model.addAttribute("url", "/make_restime");
		return "/manager/make_restime";
	}

	@PostMapping("/make_restime")
	public String insertResTime(BusinessHourVO restime,  @AuthenticationPrincipal RestaurantManagerVO manager) {
		restime.setBh_rt_num(manager.getRm_rt_num());
		System.out.println(manager);
		System.out.println(restime);
		
		int rtNum = manager.getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    restime.setBh_rt_num(rtNum);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }

		if(managerService.makeResTiem(restime)){
			System.out.println("bh_start = " + restime.getBh_start());
			System.out.println("bh_state = " + restime.isBh_state());
			return "redirect:/manager/restimelist/"+rtNum;
		}

		return "/manager/make_restime/";
	}

	//날짜 입력 처리(HH:mm)
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
