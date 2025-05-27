package kr.kh.tableup.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.kh.tableup.model.util.CustomManager;
import kr.kh.tableup.model.vo.BusinessDateVO;
import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.FacilityVO;
import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.MenuTypeVO;
import kr.kh.tableup.model.vo.MenuVO;
import kr.kh.tableup.model.vo.PaymentVO;
import kr.kh.tableup.model.vo.RegionVO;
import kr.kh.tableup.model.vo.ResCouponVO;
import kr.kh.tableup.model.vo.ResNewsVO;
import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.RestaurantFacilityVO;
import kr.kh.tableup.model.vo.RestaurantManagerVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.service.ManagerService;
import kr.kh.tableup.service.PaymentService;
import kr.kh.tableup.service.ReservationService;







@Controller
@RequestMapping("/manager")
public class ManagerController {
	
	@Autowired
	ManagerService managerService;

	@Autowired
	ReservationService reservationService;

	@Autowired
	PaymentService paymentService;

	

	@GetMapping({"", "/"})
	public String manager(Model model, @AuthenticationPrincipal CustomManager manager) {
		System.out.println(manager);
		// model.addAttribute("url","/main");
		model.addAttribute("manager", manager);
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
		if(managerService.insertManager(rm)){
			return "redirect:/manager/login";
		}
		return "redirect:/manager/signup";
	}
	
	@GetMapping("/restaurant/restaurant/{rm_id}")
	public String restaurantPage(@PathVariable("rm_id") String rm_id,Model model, Principal principal) {
		String loginId = principal.getName();
		
		if (!loginId.equals(rm_id)) {
			return "redirect:/manager";
		}
		RestaurantManagerVO manager = managerService.getManagerId(loginId);
		//해당 매니저의 매장 외래키를 가져옴
		int rm_num=manager.getRm_num();
		RestaurantVO restaurant =managerService.selectRestaurant(rm_num);
		List<FoodCategoryVO> foodcategory = managerService.getFoodCategory();
		List<RegionVO> region = managerService.getRegion();
		List<DetailRegionVO> dr = managerService.getDetailRegion();
		List<DetailFoodCategoryVO> dfc = managerService.getDetailFood();
		
		List<FileVO> fileList = managerService.getFileList(manager.getRm_rt_num());
		model.addAttribute("fileList", fileList);
		

		System.out.println(manager.getRm_id());
		System.out.println(restaurant);
		System.out.println(manager);
		
		model.addAttribute("manager", manager);
		model.addAttribute("restaurant", restaurant);
		
		
		model.addAttribute("foodcategory", foodcategory);
		model.addAttribute("region", region);
		model.addAttribute("dr", dr);
		model.addAttribute("dfc", dfc);
		
		model.addAttribute("url", "/restaurant");
		return "/manager/restaurant/restaurant";
	}

	@GetMapping("/restaurant/restaurant")
    public String redirectRestaurant(Principal principal) {
        String loginId = principal.getName();
        return "redirect:/manager/restaurant/restaurant/" + loginId;
    }

	@GetMapping("/restaurant/make")
	public String makePage(Model model, @AuthenticationPrincipal CustomManager manager) {
		List<FoodCategoryVO> foodcategory = managerService.getFoodCategory();
		List<RegionVO> region = managerService.getRegion();
		List<DetailRegionVO> dr = managerService.getDetailRegion();
		List<DetailFoodCategoryVO> dfc = managerService.getDetailFood();

		if(manager.getManager().getRm_rt_num() > 0) {
			// 매장 정보가 있는 매니저 → 매장 정보 수정 페이지로
			System.out.println("매니저 정보가 있습니다.");
			return "redirect:/manager/restaurant/remake";
		}

		System.out.println(region);
		System.out.println(dr);
		System.out.println(foodcategory);
		System.out.println(dfc);

		model.addAttribute("url", "/make");
		model.addAttribute("foodcategory", foodcategory);
		model.addAttribute("region", region);
		model.addAttribute("dr", dr);
		model.addAttribute("dfc", dfc);
		return "/manager/restaurant/make";
	}
	
	@PostMapping("/restaurant/make")
	public String insertPage(RestaurantVO restaurant,@RequestParam("fileList") MultipartFile[] fileList,  
													 @AuthenticationPrincipal CustomManager manager ) {
		System.out.println(manager);
		System.out.println(restaurant);
		

		if(managerService.insertRestaurant(restaurant, manager.getManager(), fileList)){
			return "redirect:/manager/restaurant/restaurant";
		}
		return "redirect:/manager/restaurant/make";
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

	//매장 정보 수정
	@GetMapping("/restaurant/remake")
	public String remakePage(Model model,@AuthenticationPrincipal CustomManager manager) {
		if (manager.getManager().getRm_rt_num() <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
				System.out.println("매니저 정보가 없습니다.");
        return "redirect:/manager/make";
    }
		List<FoodCategoryVO> foodcategory = managerService.getFoodCategory();
		List<RegionVO> region = managerService.getRegion();
		List<DetailRegionVO> dr = managerService.getDetailRegion();
		List<DetailFoodCategoryVO> dfc = managerService.getDetailFood();
		List<FileVO> fileList = managerService.getFileList(manager.getManager().getRm_rt_num());

		RestaurantVO restaurant = managerService.getResDetail(manager.getManager().getRm_rt_num());
		System.out.println(restaurant);


		// System.out.println(region);
		// System.out.println(dr);
		// System.out.println(foodcategory);
		// System.out.println(dfc);

		model.addAttribute("url", "/remake");
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("foodcategory", foodcategory);
		model.addAttribute("region", region);
		model.addAttribute("detailRegionList", dr);
		model.addAttribute("detailFoodList", dfc);
		model.addAttribute("fileList", fileList);

		return "/manager/restaurant/remake";
	}

	@PostMapping("/restaurant/remake")
	public String updatePage(RestaurantVO restaurant,@RequestParam("fileList") MultipartFile[] fileList,  
													 @AuthenticationPrincipal CustomManager manager ) {
		System.out.println(manager);
		System.out.println("수정할 매장 정보 :"+restaurant);

		if(managerService.updateRestaurant(restaurant, manager.getManager(), fileList)){
			return "redirect:/manager/restaurant/restaurant";
		}
		return "redirect:/manager/restaurant/remake";
	}
	
	//메뉴 리스트 출력
	@GetMapping("/menu/menulist/{rt_num}")
	public String menuListPage(Model model, @PathVariable int rt_num,  @AuthenticationPrincipal CustomManager manager) {


		
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/";
		}

		if(rt_num != manager.getManager().getRm_rt_num()){
			return "redirect:/manager/opertimelist/"+manager.getManager().getRm_rt_num();
		}

		System.out.println("menulist rt_num: " + rt_num);
		List<MenuVO> menulist = managerService.getMenuList(rt_num);
		model.addAttribute("menulist", menulist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		System.out.println(manager.getManager());
		return "manager/menu/menulist";
	}

	//메뉴 등록 페이지
	@GetMapping("/menu/make_menu")
	public String makeMenuPage(Model model, @AuthenticationPrincipal CustomManager manager) {
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/";
		}

		if (manager.getManager().getRm_rt_num() <= 0) {
			// 매장 정보가 없는 매니저 → 매장 등록 페이지로
			System.out.println("매니저 정보가 없습니다.");
			return "redirect:/manager/make";
    }
		List<MenuTypeVO> menutype = managerService.getMenuTypeList();
		
		model.addAttribute("url", "/make_menu");
		model.addAttribute("menutype", menutype);
		return "/manager/menu/make_menu";
	}

	@PostMapping("/menu/make_menu")
	public String insertMenu(MenuVO menu, MultipartFile mn_img2,  @AuthenticationPrincipal CustomManager manager) {
		menu.setMn_rt_num(manager.getManager().getRm_rt_num());
		System.out.println(manager);
		System.out.println(menu);
		System.out.println(mn_img2.getOriginalFilename());
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    menu.setMn_rt_num(rtNum);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
				System.out.println("매니저 정보가 없습니다.");
        return "redirect:/manager/make";
    }

		if(managerService.makeMenu(menu,mn_img2)){
			return "redirect:/manager/menu/menulist/"+rtNum;
		}

		return "/manager/menu/make_menu/";
	}
	
	//메뉴 상세 페이지
	@GetMapping("/menu/menu/{mn_num}")
	public String detailMenu(Model model, @PathVariable int mn_num) {
		MenuVO menu = managerService.getMenu(mn_num);
		MenuTypeVO menutype =managerService.getMenuType(menu.getMn_mt_num());
		model.addAttribute("menu", menu);
		model.addAttribute("menutype", menutype);
		return "/manager/menu/menu";
	}

	//메뉴 삭제
	@PostMapping("/menu/delete_menu/{mn_num}")
	public String deleteMenuPage(@AuthenticationPrincipal CustomManager manager, @PathVariable int mn_num) {
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteMenu(mn_num)) {
        return "redirect:/manager/menu/menulist/"+rtNum;
    }
		return "redirect:/manager/menu/menu?mn_num=" + mn_num;
	}
	

	//메뉴 수정 페이지
	@GetMapping("/menu/remake_menu")
	public String reMakeMenuPage(Model model, @AuthenticationPrincipal CustomManager manager, @RequestParam("mn_num") int mn_num) {
		List<MenuTypeVO> menutype = managerService.getMenuTypeList();
		MenuVO menu = managerService.getMenu(mn_num);
		System.out.println(menu);

		model.addAttribute("url", "/remake_menu");
		model.addAttribute("menutype", menutype);
		model.addAttribute("menu", menu);
		return "/manager/menu/remake_menu";
	}
	
	@PostMapping("/menu/remake_menu")
	public String updateMenu(MenuVO menu, MultipartFile mn_img2,  @AuthenticationPrincipal CustomManager manager) {
		menu.setMn_rt_num(manager.getManager().getRm_rt_num());
		System.out.println(manager);
		System.out.println(menu);
		System.out.println(mn_img2.getOriginalFilename());
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    menu.setMn_rt_num(rtNum);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }

		if(managerService.updateMenu(menu,mn_img2)){
			return "redirect:/manager/menu/menulist/"+rtNum;
		}
		
		return "/manager/menu/remake_menu";
	}
	
	//매장 상세 정보 출력
	@GetMapping("/detail/restaurantdetail/{rt_num}")
	public String restaurantDetailPage(Model model, @PathVariable int rt_num, @AuthenticationPrincipal CustomManager manager) {


		
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/";
		}

		if(rt_num != manager.getManager().getRm_rt_num()){
			return "redirect:/manager/opertimelist/"+manager.getManager().getRm_rt_num();
		}

		RestaurantVO resdetail = managerService.getResDetail(rt_num);
		if(resdetail != null) model.addAttribute("resdetail", resdetail);
		model.addAttribute("manager", manager.getManager());
		return "/manager/detail/restaurantdetail";
	}

	@GetMapping("/detail/make_detail")
	public String makeResDetailPage(Model model) {
		model.addAttribute("url", "/make_detail");
		return "/manager/detail/make_detail";
	}
	
	@PostMapping("/detail/make_detail")
	public String insertResDetailPage(RestaurantVO resdetail, @AuthenticationPrincipal CustomManager manager ) {
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 정보: " + manager);
		System.out.println("매니저의 매장 번호: " + rtNum);
    resdetail.setRt_num(rtNum);
		resdetail.setRd_rt_num(rtNum);
		
		System.out.println(manager);
		System.out.println(resdetail);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }
		if(managerService.insertResDetail(resdetail)){
			return "redirect:/manager/detail/restaurantdetail/"+ rtNum;
		}
		return "redirect:/manager/detail/make_detail";
	}

	//상세정보 변경 페이지
	@GetMapping("/detail/remake_detail")
	public String reMakeDetailPage(Model model, @AuthenticationPrincipal CustomManager manager) {

		int rtNum = manager.getManager().getRm_rt_num();
		System.out.println("매니저의 매장 번호: " + rtNum);
		RestaurantVO resdetail = managerService.getResDetail(rtNum);

		model.addAttribute("res", resdetail);
		System.out.println(resdetail);
		model.addAttribute("url", "/remake_detail");
		return "/manager/detail/remake_detail";
	}
	
	@PostMapping("/detail/remake_detail")
	public String updateDetail(RestaurantVO resdetail, @AuthenticationPrincipal CustomManager manager) {
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    resdetail.setRd_rt_num(rtNum);
		
		System.out.println(manager);
		System.out.println(resdetail);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }

		if(managerService.updateDetail(resdetail)){
			return "redirect:/manager/detail/restaurantdetail/"+ rtNum;
		}
		System.out.println("수정 실패");
		return "/manager/detail/remake_detail";
	}

	//매니저 쿠폰 페이지
	//쿠폰 리스트
	@GetMapping("/coupon/couponlist/{rt_num}")
	public String couponListPage(Model model, @PathVariable int rt_num,  @AuthenticationPrincipal CustomManager manager) {


		
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/";
		}

		if(rt_num != manager.getManager().getRm_rt_num()){
			return "redirect:/manager/opertimelist/"+manager.getManager().getRm_rt_num();
		}


		System.out.println("couponlist rt_num: " + rt_num);
		List<ResCouponVO> couponlist = managerService.getCouponList(rt_num);
		
		model.addAttribute("couponlist", couponlist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		return "manager/coupon/couponlist";
	}

	//쿠폰 등록 페이지
	@GetMapping("/coupon/make_coupon")
	public String makeCouponPage(Model model) {
		model.addAttribute("url", "/make_coupon");
		return "/manager/coupon/make_coupon";
	}

	@PostMapping("/coupon/make_coupon")
	public String insertCoupon(ResCouponVO coupon,  @AuthenticationPrincipal CustomManager manager) {
		coupon.setRec_rt_num(manager.getManager().getRm_rt_num());
		System.out.println(manager);
		System.out.println(coupon);
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    coupon.setRec_rt_num(rtNum);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
				System.out.println("매니저 정보가 없습니다.");
        return "redirect:/manager/make";
    }

		if(managerService.makeCoupon(coupon)){
			return "redirect:/manager/coupon/couponlist/"+rtNum;
		}

		return "/manager/coupon/make_coupon/";
	}

	//쿠폰 정보 출력 페이지
	@GetMapping("/coupon/coupon/{rec_num}")
	public String detailCoupon(Model model, @PathVariable int rec_num) {
		ResCouponVO coupon = managerService.getCoupon(rec_num);
		model.addAttribute("coupon", coupon);
		return "/manager/coupon/coupon";
	}

	//쿠폰 수정 페이지
	@GetMapping("/coupon/remake_coupon/{rec_num}")
	public String reMakeCouponPage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int rec_num) {
		ResCouponVO coupon = managerService.getCoupon(rec_num);
    System.out.println(coupon);
		model.addAttribute("coupon", coupon);
		model.addAttribute("url", "/remake_coupon");
		return "/manager/coupon/remake_coupon";
	}
	
	@PostMapping("/coupon/remake_coupon")
	public String updateCoupon(ResCouponVO coupon,  @AuthenticationPrincipal CustomManager manager) {
		coupon.setRec_rt_num(manager.getManager().getRm_rt_num());
		System.out.println(manager);
		System.out.println(coupon);
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    coupon.setRec_rt_num(rtNum);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }

		if(managerService.updateCoupon(coupon)){
			return "redirect:/manager/coupon/couponlist/"+rtNum;
		}
		
		return "/manager/coupon/remake_coupon";
	}

	//쿠폰 삭제
	@PostMapping("/delete_coupon/{rec_num}")
	public String deleteCouponPage(@AuthenticationPrincipal CustomManager manager, @PathVariable int rec_num) {
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteCoupon(rec_num)) {
        return "redirect:/manager/coupon/couponlist/"+rtNum;
    }
		return "redirect:/manager/coupon/coupon?rec_num=" + rec_num;
	}

	//매장 소식 페이지
	//매장 소식 리스트
	@GetMapping("/news/newslist/{rt_num}")
	public String newsListPage(Model model, @PathVariable int rt_num,  @AuthenticationPrincipal CustomManager manager) {


		
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/";
		}

		if(rt_num != manager.getManager().getRm_rt_num()){
			return "redirect:/manager/opertimelist/"+manager.getManager().getRm_rt_num();
		}

		System.out.println("newslist rt_num: " + rt_num);
		List<ResNewsVO> newslist = managerService.getNewsList(rt_num);

		model.addAttribute("newslist", newslist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		return "manager/news/newslist";
	}
	@GetMapping("/news/make_news")
	public String makeNewsPage(Model model) {
		model.addAttribute("url", "/make_news");
		return "/manager/news/make_news";
	}

	@PostMapping("/news/make_news")
	public String insertCoupon(ResNewsVO news,  @AuthenticationPrincipal CustomManager manager) {
		news.setRn_rt_num(manager.getManager().getRm_rt_num());
		System.out.println(manager);
		System.out.println(news);
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    news.setRn_rt_num(rtNum);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
				System.out.println("매니저 정보가 없습니다.");
        return "redirect:/manager/make";
    }

		if(managerService.makeNews(news)){
			return "redirect:/manager/news/newslist/"+rtNum;
		}

		return "/manager/news/make_news/";
	}

	//소식 정보 출력 페이지
	@GetMapping("/news/news/{rn_num}")
	public String detailNews(Model model, @PathVariable int rn_num) {
		ResNewsVO news = managerService.getNews(rn_num);
		System.out.println(news);
		model.addAttribute("news", news);
		return "/manager/news/news";
	}

	//소식 수정 페이지
	@GetMapping("/news/remake_news/{rn_num}")
	public String reMakeNewsPage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int rn_num) {
		ResNewsVO news = managerService.getNews(rn_num);
    System.out.println(news);
		model.addAttribute("news", news);
		model.addAttribute("url", "/news/remake_news");
		return "/manager/news/remake_news";
	}
	
	@PostMapping("/news/remake_news")
	public String updateNews(ResNewsVO news,  @AuthenticationPrincipal CustomManager manager) {
		news.setRn_rt_num(manager.getManager().getRm_rt_num());
		System.out.println(manager);
		System.out.println(news);
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    news.setRn_rt_num(rtNum);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }

		if(managerService.updateNews(news)){
			return "redirect:/manager/news/newslist/"+rtNum;
		}
		
		return "/manager/news/remake_news";
	}

	//소식 삭제
	@PostMapping("/news/delete_news/{rn_num}")
	public String deleteNewsPage(@AuthenticationPrincipal CustomManager manager, @PathVariable int rn_num) {
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteNews(rn_num)) {
        return "redirect:/manager/news/newslist/"+rtNum;
    }
		return "redirect:/manager/news/news?rec_num=" + rn_num;
	}


	//예약 가능 시간 페이지
	//예약 가능 시간 목록(리스트)
	@GetMapping("/restime/restimelist/{rt_num}")
	public String ResTimeListPage(Model model, @PathVariable int rt_num,  @AuthenticationPrincipal CustomManager manager) {
		System.out.println("restimelist rt_num: " + rt_num);
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/";
		}

		if(rt_num != manager.getManager().getRm_rt_num()){
			return "redirect:/manager/restime/restimelist/"+manager.getManager().getRm_rt_num();
		}


		List<BusinessHourVO> restimelist = managerService.getResTimeList(rt_num);



		model.addAttribute("restimelist", restimelist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		return "manager/restime/restimelist";
	}

	//예약 시간 등록 페이지
	@GetMapping("/restime/make_restime")
	public String makeResTimePage(Model model) {
		model.addAttribute("url", "/make_restime");
		return "/manager/restime/make_restime";
	}

	@PostMapping("/restime/make_restime")
	public String insertResTime(BusinessHourVO restime,  @AuthenticationPrincipal CustomManager manager) {
		restime.setBh_rt_num(manager.getManager().getRm_rt_num());
		System.out.println(manager.getManager());
		System.out.println(restime);
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    restime.setBh_rt_num(rtNum);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }

		if(managerService.makeResTiem(restime)){
			System.out.println("bh_start = " + restime.getBh_start());
			System.out.println("bh_state = " + restime.isBh_state());
			return "redirect:/manager/restime/restimelist/"+rtNum;
		}

		return "/manager/restime/make_restime/";
	}
	//예약 시간 변경
	@GetMapping("/restime/remake_restime/{bh_num}")
	public String reMakeResTimePage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int bh_num) {
		BusinessHourVO restime = managerService.getBusinessHour(bh_num);
    System.out.println(restime);
		model.addAttribute("restime", restime);
		model.addAttribute("url", "/remake_restime");
		return "/manager/restime/remake_restime";
	}
	
	@PostMapping("/restime/remake_restime")
	public String updateRestime(BusinessHourVO restime,  @AuthenticationPrincipal CustomManager manager ) {
		restime.setBh_rt_num(manager.getManager().getRm_rt_num());
		System.out.println(manager);
		System.out.println(restime);
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    restime.setBh_rt_num(rtNum);
		System.out.println("수정할 bh_num = " + restime.getBh_num());

    

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }
		if(managerService.remakeResTime(restime)){
			System.out.println("bh_start = " + restime.getBh_start());
			System.out.println("bh_state = " + restime.isBh_state());
			return "redirect:/manager/restime/restimelist/"+rtNum;
		}
		return "/manager/restime/remake_restime";
	}

	//예약 가능 시간 삭제
	@PostMapping("/restime/delete_restime/{bh_num}")
	public String deleteResTime(@AuthenticationPrincipal CustomManager manager, @PathVariable int bh_num) {
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteResTime(bh_num)) {
        return "redirect:/manager/restime/restimelist/"+rtNum;
    }
		return "redirect:/manager/restime/restimelist/"+rtNum;
	}

	//날짜 입력 처리(HH:mm)
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	//영업 일자 페이지
	@GetMapping("/opertime/opertimelist/{rt_num}")
	public String OperTimePage(Model model, @PathVariable int rt_num, @AuthenticationPrincipal CustomManager manager) {

		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/";
		}

		if(rt_num != manager.getManager().getRm_rt_num()){
			return "redirect:/manager/opertimelist/"+manager.getManager().getRm_rt_num();
		}


		System.out.println("opertimelist rt_num: " + rt_num);
		List<BusinessDateVO> opertimelist = managerService.getOperTimeList(rt_num);
		System.out.println(opertimelist);
		model.addAttribute("opertimelist", opertimelist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		return "manager/opertime/opertimelist";
	}

	//영업 일자 등록 페이지
	@GetMapping("/make_opertime")
	public String makeOperTimePage(Model model, @AuthenticationPrincipal CustomManager manager) {
		List<BusinessDateVO> opertimelist = managerService.getOperTimeList(manager.getManager().getRm_rt_num());
		
		model.addAttribute("opertimelist", opertimelist);
		model.addAttribute("url", "/make_opertime");
		return "/manager/opertime/make_opertime";
	}


	@GetMapping("/make_opertime_sub") 
	public String makeOperTimeSubPage(Model model,  @AuthenticationPrincipal CustomManager manager) {
		if(manager.getManager().getRm_id() == null) {
			return "/manager/login";
		}
		model.addAttribute("managerId", manager.getManager().getRm_id());
		model.addAttribute("url", "/make_opertime_sub");
		return "/manager/opertime/make_opertime_sub";
	}


	@PostMapping("/opertime/make_opertime")
	public String insertOperTime(BusinessDateVO opertime,  @AuthenticationPrincipal CustomManager manager) {
		opertime.setBd_rt_num(manager.getManager().getRm_rt_num());
		System.out.println(manager.getManager());
		System.out.println(opertime);
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    opertime.setBd_rt_num(rtNum);

    if (rtNum <= 0) {
				
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/restaurant/make";
    }

		if(managerService.makeOperTime(opertime)){
			System.out.println("bd_date = " + opertime.getBd_date());
			return "redirect:/manager/opertimelist/"+rtNum;
		}

		return "/manager/opertime/make_opertime";
	}


	@PostMapping("/opertime/make_opertime_list")
	@ResponseBody
	public String insertOperTimeList(
		@RequestBody List<BusinessDateVO> operList
			/*, @AuthenticationPrincipal CustomManager manager*/
	) {
			// List<BusinessDateVO> operList = new ArrayList<>();
			System.out.println("ajax 수신");
			/*if (manager == null || manager.getManager() == null) {
					return "로그인 정보가 없습니다.";
			}*/
			if (operList == null || operList.isEmpty()) {
					return "등록할 영업일자가 없습니다.";
			}
			System.out.println("opertimelist: " + operList);
			

			//int rtNum = manager.getManager().getRm_rt_num();
			int rtNum = 1;
			int result = 0;
			for (BusinessDateVO oper : operList) {
					oper.setBd_rt_num(rtNum); 
					if (oper.getBd_date() == null || oper.getBd_open() == null || oper.getBd_close() == null) {
							continue; 
					}

					managerService.makeOperTime(oper); // insert 로직 호출
					result++;
			}

			return "등록 성공" + result + "건";
	}


	//영업 일자 변경
	@GetMapping("/opertime/remake_opertime/{bd_num}")
	public String reMakeOperTimePage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int bd_num) {

		BusinessDateVO opertime = managerService.getBusinessDate(bd_num);
		if(opertime == null) {
			return "redirect:/manager/opertimelist/"+manager.getManager().getRm_rt_num();
		}

		if(manager.getManager().getRm_rt_num() != opertime.getBd_rt_num()) {
			return "redirect:/manager/opertimelist/"+manager.getManager().getRm_rt_num();
		}

    System.out.println(opertime);
		System.out.println(opertime.getBd_open_ts());
		model.addAttribute("opertime", opertime);
		model.addAttribute("url", "/remake_opertime");
		return "/manager/opertime/remake_opertime";
	}
	
	@PostMapping("/opertime/remake_opertime")
	public String updateOpertime(BusinessDateVO opertime,  @AuthenticationPrincipal CustomManager manager ) {
		opertime.setBd_rt_num(manager.getManager().getRm_rt_num());
		System.out.println(manager);
		System.out.println(opertime);
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    opertime.setBd_rt_num(rtNum);
		System.out.println("수정할 bd_num = " + opertime.getBd_num());

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }
		 if(managerService.remakeOperTime(opertime)){
		 	return "redirect:/manager/opertime/opertimelist/"+rtNum;
		 }
		return "/manager/opertime/remake_opertime";
	}

	//영업 시간 삭제
	@PostMapping("/opertime/delete_opertime/{bd_num}")
	public String deleteOperTime(@AuthenticationPrincipal CustomManager manager, @PathVariable int bd_num) {
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteOperTime(bd_num)) {
        return "redirect:/manager/opertime/opertimelist/"+rtNum;
    }
		return "redirect:/manager/opertime/opertimelist/"+rtNum;
	}

	//매니저페이지
	@GetMapping("/managerpage")
	public String managerPage(Model model, @AuthenticationPrincipal CustomManager manager) {
		RestaurantManagerVO rm = manager.getManager();
		model.addAttribute("rm", rm);

		return "/manager/managerpage";
	}
	
	@PostMapping("/managerpage")
	public String PostmanagerPage(Model model, @AuthenticationPrincipal CustomManager manager, RestaurantManagerVO rm) {
		RestaurantManagerVO currentManager = manager.getManager();

		if(!currentManager.getRm_id().equals(rm.getRm_id())){
			return "redirect:/manager/managerpage";
		}

		managerService.updateManagerInfo(rm);

		currentManager.setRm_email(rm.getRm_email());
		currentManager.setRm_name(rm.getRm_name());
		currentManager.setRm_phone(rm.getRm_phone());

		return "manager/main";
	}
	
	//매장 편의시설 목록 페이지
	@GetMapping("/resfacility/resfacilitylist/{rt_num}")
	public String facilityListPage(Model model, @PathVariable int rt_num, @AuthenticationPrincipal CustomManager manager) {
		
		
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/";
		}

		if(rt_num != manager.getManager().getRm_rt_num()){
			return "redirect:/manager/opertimelist/"+manager.getManager().getRm_rt_num();
		}
		
		
		System.out.println("resfacilitylist rt_num: " + rt_num);
		List<FacilityVO> facility = managerService.getFacilityList();
		List<RestaurantFacilityVO> facilitylist = managerService.getResFacilityList(rt_num);

		System.out.println("등록된 편의 시설 : "+facility);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("facility", facility);
		model.addAttribute("facilitylist", facilitylist);
		model.addAttribute("manager", manager.getManager());
		return "manager/resfacility/resfacilitylist";
	}

	//매장 편의시설 등록 페이지
	@GetMapping("/resfacility/make_resfacility")
	public String makeResFacilityPage(Model model) {
		List<FacilityVO> facility = managerService.getFacilityList();

		model.addAttribute("facility", facility);
		model.addAttribute("url", "/make_resfacility");
		return "/manager/resfacility/make_resfacility";
	}

	@PostMapping("/resfacility/make_resfacility")
	public String insertResFacility(RestaurantFacilityVO resfacility,  @AuthenticationPrincipal CustomManager manager) {
		resfacility.setRf_rt_num(manager.getManager().getRm_rt_num());
		System.out.println(manager.getManager());
		System.out.println(resfacility);
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
  	resfacility.setRf_rt_num(rtNum);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }

		if(managerService.makeResFacility(resfacility)){
			return "redirect:/manager/resfacility/resfacilitylist/"+rtNum;
		}

		return "/manager/resfacility/make_resfacility";
	}

	//편의시설 정보 변경
	@GetMapping("/resfacility/remake_resfacility/{rf_num}")
	public String reMakeResFacilityPage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int rf_num) {
		RestaurantFacilityVO resfacility = managerService.getResFacility(rf_num);
		List<FacilityVO> facility = managerService.getFacilityList();
		System.out.println(resfacility);
		
		model.addAttribute("facility", facility);
		model.addAttribute("resfacility", resfacility);
		model.addAttribute("url", "/remake_resfacility");
		return "/manager/resfacility/remake_resfacility";
	}
	
	@PostMapping("/resfacility/remake_resfacility")
	public String updateResFacility(RestaurantFacilityVO resfacility,  @AuthenticationPrincipal CustomManager manager ) {
		resfacility.setRf_rt_num(manager.getManager().getRm_rt_num());
		System.out.println(manager.getManager());
		System.out.println(resfacility);
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
		System.out.println("-------------------------");
  	//resfacility.setRf_rt_num(rtNum);
		System.out.println("수정할 rf_num = " + resfacility.getRf_num());

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }
		 if(managerService.remakeResFacility(resfacility)){
		 	return "redirect:/manager/resfacility/resfacilitylist/"+rtNum;
		 }
		return "/manager/resfacility/remake_resfacility";
	}

	@PostMapping("/resfacility/delete_resfacility/{rf_num}")
	public String deleteResFacility(@AuthenticationPrincipal CustomManager manager, @PathVariable int rf_num) {
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteResFacility(rf_num)) {
        return "redirect:/manager/resfacility/resfacilitylist/"+rtNum;
    }
		return "redirect:/manager/resfacility/resfacilitylist/"+rtNum;
	}

	@GetMapping("/manager_pay/pay")
	public String paymentPage(Model model, @AuthenticationPrincipal CustomManager manager, Principal principal){
		if(principal == null || manager.getManager().getRm_rt_num()==0 || 
			 manager == null || manager.getManager() == null){
			return "redirect:/manager/login";
		}

		List<PaymentVO> paymentList = paymentService.getPaymentList(manager.getManager().getRm_rt_num());
		RestaurantVO restaurant = managerService.getRestaurantByNum(manager.getManager().getRm_rt_num());
		System.out.println("예약 결제 내역"+paymentList);

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("manager",manager.getManager());
		model.addAttribute("paymentList", paymentList);
		return "/manager/manager_pay/pay";
	}
	
	
	@GetMapping("/manager_reservation")
	public String reservateionManager(Model model) {
		
		return "/manager/reservation/manager_reservation";
	}

	@GetMapping("/reservation")
	public String reservateion(Model model) {
		
		return "/manager/reservation/reservation";
	}
	@PostMapping("/reservation/date")
	public String getMethodName(Model model, @AuthenticationPrincipal CustomManager customManager, @RequestParam String date) {
		List<ReservationVO> reservations = reservationService.getReservationList(customManager, date);
		model.addAttribute("date", date);
		model.addAttribute("reservations",reservations);
		System.out.println("reservaions = " + reservations);
		return "/manager/reservation/reservation_date";
	}
	
}
