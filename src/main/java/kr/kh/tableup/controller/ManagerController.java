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

import kr.kh.tableup.model.util.CustomManager;
import kr.kh.tableup.model.util.CustomUser;
import kr.kh.tableup.model.vo.BusinessDateVO;
import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.FacilityVO;
import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.MenuTypeVO;
import kr.kh.tableup.model.vo.MenuVO;
import kr.kh.tableup.model.vo.RegionVO;
import kr.kh.tableup.model.vo.ResCouponVO;
import kr.kh.tableup.model.vo.ResNewsVO;
import kr.kh.tableup.model.vo.RestaurantDetailVO;
import kr.kh.tableup.model.vo.RestaurantFacilityVO;
import kr.kh.tableup.model.vo.RestaurantManagerVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.service.ManagerService;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
		managerService.insertManager(rm);
		return "redirect:/manager/login";
	}
	
	@GetMapping("/restaurant/{rm_id}")
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
		// List<FileVO> fileList = managerService.getFileList(manager.getRm_rt_num());

		System.out.println(manager.getRm_id());
		System.out.println(restaurant);
		System.out.println(manager);
		
		model.addAttribute("manager", manager);
		model.addAttribute("restaurant", restaurant);
		// model.addAttribute("fileList", fileList);
		
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
													 @AuthenticationPrincipal CustomManager manager ) {
		System.out.println(manager);
		System.out.println(restaurant);

		if(managerService.insertRestaurant(restaurant, manager.getManager(), fileList)){
			return "redirect:/manager/restaurant";
		}
		return "redirect:/manager/make";
	}
	
	//메뉴 리스트 출력
	@GetMapping("/menulist/{rt_num}")
	public String menuListPage(Model model, @PathVariable int rt_num,  @AuthenticationPrincipal CustomManager manager) {
		System.out.println("menulist rt_num: " + rt_num);
		List<MenuVO> menulist = managerService.getMenuList(rt_num);
		model.addAttribute("menulist", menulist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		System.out.println(manager.getManager());
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
	public String deleteMenuPage(@AuthenticationPrincipal CustomManager manager, @PathVariable int mn_num) {
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteMenu(mn_num)) {
        return "redirect:/manager/menulist/"+rtNum;
    }
		return "redirect:/manager/menu?mn_num=" + mn_num;
	}
	

	//메뉴 수정 페이지
	@GetMapping("/remake_menu")
	public String reMakeMenuPage(Model model, @AuthenticationPrincipal CustomManager manager, @RequestParam("mn_num") int mn_num) {
		List<MenuTypeVO> menutype = managerService.getMenuTypeList();
		MenuVO menu = managerService.getMenu(mn_num);
		System.out.println(menu);

		model.addAttribute("url", "/remake_menu");
		model.addAttribute("menutype", menutype);
		model.addAttribute("menu", menu);
		return "/manager/remake_menu";
	}
	
	@PostMapping("/remake_menu")
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
			return "redirect:/manager/menulist/"+rtNum;
		}
		
		return "/manager/remake_menu";
	}
	
	//매장 상세 정보 출력
	@GetMapping("/restaurantdetail/{rt_num}")
	public String restaurantDetailPage(Model model, @PathVariable int rt_num, @AuthenticationPrincipal CustomManager manager) {
		RestaurantVO resdetail = managerService.getResDetail(rt_num);
		if(resdetail != null) model.addAttribute("resdetail", resdetail);
		model.addAttribute("manager", manager.getManager());
		return "/manager/restaurantdetail";
	}

	@GetMapping("/make_detail")
	public String makeResDetailPage(Model model) {
		model.addAttribute("url", "/make_detail");
		return "/manager/make_detail";
	}
	
	@PostMapping("/make_detail")
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
			return "redirect:/manager/restaurantdetail/"+ rtNum;
		}
		return "redirect:/manager/make_detail";
	}

	//상세정보 변경 페이지
	@GetMapping("/remake_detail")
	public String reMakeDetailPage(Model model, @AuthenticationPrincipal CustomManager manager) {

		model.addAttribute("url", "/remake_detail");
		return "/manager/remake_detail";
	}
	
	@PostMapping("/remake_detail")
	public String updateDetail(RestaurantVO resdetail, @AuthenticationPrincipal CustomManager manager ) {
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
			return "redirect:/manager/restaurantdetail/"+ rtNum;
		}
		System.out.println("수정 실패");
		return "/manager/remake_detail";
	}

	//매니저 쿠폰 페이지
	//쿠폰 리스트
	@GetMapping("/couponlist/{rt_num}")
	public String couponListPage(Model model, @PathVariable int rt_num,  @AuthenticationPrincipal CustomManager manager) {
		System.out.println("couponlist rt_num: " + rt_num);
		List<ResCouponVO> couponlist = managerService.getCouponList(rt_num);
		
		model.addAttribute("couponlist", couponlist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		return "manager/couponlist";
	}

	//쿠폰 등록 페이지
	@GetMapping("/make_coupon")
	public String makeCouponPage(Model model) {
		model.addAttribute("url", "/make_coupon");
		return "/manager/make_coupon";
	}

	@PostMapping("/make_coupon")
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
			return "redirect:/manager/couponlist/"+rtNum;
		}

		return "/manager/make_coupon/";
	}

	//쿠폰 정보 출력 페이지
	@GetMapping("/coupon/{rec_num}")
	public String detailCoupon(Model model, @PathVariable int rec_num) {
		ResCouponVO coupon = managerService.getCoupon(rec_num);
		model.addAttribute("coupon", coupon);
		return "/manager/coupon";
	}

	//쿠폰 수정 페이지
	@GetMapping("/remake_coupon/{rec_num}")
	public String reMakeCouponPage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int rec_num) {
		ResCouponVO coupon = managerService.getCoupon(rec_num);
    System.out.println(coupon);
		model.addAttribute("coupon", coupon);
		model.addAttribute("url", "/remake_coupon");
		return "/manager/remake_coupon";
	}
	
	@PostMapping("/remake_coupon")
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
			return "redirect:/manager/couponlist/"+rtNum;
		}
		
		return "/manager/remake_coupon";
	}

	//쿠폰 삭제
	@PostMapping("/coupon/delete_coupon/{rec_num}")
	public String deleteCouponPage(@AuthenticationPrincipal CustomManager manager, @PathVariable int rec_num) {
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteCoupon(rec_num)) {
        return "redirect:/manager/couponlist/"+rtNum;
    }
		return "redirect:/manager/coupon?rec_num=" + rec_num;
	}

	//매장 소식 페이지
	//매장 소식 리스트
	@GetMapping("/newslist/{rt_num}")
	public String newsListPage(Model model, @PathVariable int rt_num,  @AuthenticationPrincipal CustomManager manager) {
		System.out.println("newslist rt_num: " + rt_num);
		List<ResNewsVO> newslist = managerService.getNewsList(rt_num);

		model.addAttribute("newslist", newslist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		return "manager/newslist";
	}
	@GetMapping("/make_news")
	public String makeNewsPage(Model model) {
		model.addAttribute("url", "/make_news");
		return "/manager/make_news";
	}

	@PostMapping("/make_news")
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
			return "redirect:/manager/newslist/"+rtNum;
		}

		return "/manager/make_news/";
	}

	//소식 정보 출력 페이지
	@GetMapping("/news/{rn_num}")
	public String detailNews(Model model, @PathVariable int rn_num) {
		ResNewsVO news = managerService.getNews(rn_num);
		System.out.println(news);
		model.addAttribute("news", news);
		return "/manager/news";
	}

	//소식 수정 페이지
	@GetMapping("/remake_news/{rn_num}")
	public String reMakeNewsPage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int rn_num) {
		ResNewsVO news = managerService.getNews(rn_num);
    System.out.println(news);
		model.addAttribute("news", news);
		model.addAttribute("url", "/remake_news");
		return "/manager/remake_news";
	}
	
	@PostMapping("/remake_news")
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
			return "redirect:/manager/newslist/"+rtNum;
		}
		
		return "/manager/remake_news";
	}

	//소식 삭제
	@PostMapping("/news/delete_news/{rn_num}")
	public String deleteNewsPage(@AuthenticationPrincipal CustomManager manager, @PathVariable int rn_num) {
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteNews(rn_num)) {
        return "redirect:/manager/newslist/"+rtNum;
    }
		return "redirect:/manager/news?rec_num=" + rn_num;
	}


	//예약 가능 시간 페이지
	//예약 가능 시간 목록(리스트)
	@GetMapping("/restimelist/{rt_num}")
	public String ResTimeListPage(Model model, @PathVariable int rt_num,  @AuthenticationPrincipal CustomManager manager) {
		System.out.println("restimelist rt_num: " + rt_num);
		List<BusinessHourVO> restimelist = managerService.getResTimeList(rt_num);

		model.addAttribute("restimelist", restimelist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		return "manager/restimelist";
	}

	//예약 시간 등록 페이지
	@GetMapping("/make_restime")
	public String makeResTimePage(Model model) {
		model.addAttribute("url", "/make_restime");
		return "/manager/make_restime";
	}

	@PostMapping("/make_restime")
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
			return "redirect:/manager/restimelist/"+rtNum;
		}

		return "/manager/make_restime/";
	}
	//예약 시간 변경
	@GetMapping("/remake_restime/{bh_num}")
	public String reMakeResTimePage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int bh_num) {
		BusinessHourVO restime = managerService.getBusinessHour(bh_num);
    System.out.println(restime);
		model.addAttribute("restime", restime);
		model.addAttribute("url", "/remake_restime");
		return "/manager/remake_restime";
	}
	
	@PostMapping("/remake_restime")
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
			return "redirect:/manager/restimelist/"+rtNum;
		}
		return "/manager/remake_restime";
	}

	//예약 가능 시간 삭제
	@PostMapping("/delete_restime/{bh_num}")
	public String deleteResTime(@AuthenticationPrincipal CustomManager manager, @PathVariable int bh_num) {
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteResTime(bh_num)) {
        return "redirect:/manager/restimelist/"+rtNum;
    }
		return "redirect:/manager/restimelist/"+rtNum;
	}

	//날짜 입력 처리(HH:mm)
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	//영업 일자 페이지
	@GetMapping("/opertimelist/{rt_num}")
	public String OperTimePage(Model model, @PathVariable int rt_num, @AuthenticationPrincipal CustomManager manager) {
		System.out.println("opertimelist rt_num: " + rt_num);
		List<BusinessDateVO> opertimelist = managerService.getOperTimeList(rt_num);

		model.addAttribute("opertimelist", opertimelist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		return "manager/opertimelist";
	}

	//영업 일자 등록 페이지
	@GetMapping("/make_opertime")
	public String makeOperTimePage(Model model) {
		
		model.addAttribute("url", "/make_opertime");
		return "/manager/make_opertime";
	}

	@PostMapping("/make_opertime")
	public String insertOperTime( BusinessDateVO opertime,  @AuthenticationPrincipal CustomManager manager) {
		opertime.setBd_rt_num(manager.getManager().getRm_rt_num());
		System.out.println(manager.getManager());
		System.out.println(opertime);
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    opertime.setBd_rt_num(rtNum);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
        return "redirect:/manager/make";
    }

		if(managerService.makeOperTime(opertime)){
			return "redirect:/manager/opertimelist/"+rtNum;
		}

		return "/manager/make_opertime/";
	}

	//영업 일자 변경
	@GetMapping("/remake_opertime/{bd_num}")
	public String reMakeOperTimePage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int bd_num) {
		BusinessDateVO opertime = managerService.getBusinessDate(bd_num);
    System.out.println(opertime);
		model.addAttribute("opertime", opertime);
		model.addAttribute("url", "/remake_opertime");
		return "/manager/remake_opertime";
	}
	
	@PostMapping("/remake_opertime")
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
		 	return "redirect:/manager/opertimelist/"+rtNum;
		 }
		return "/manager/remake_opertime";
	}

	//영업 시간 삭제
	@PostMapping("/delete_opertime/{bd_num}")
	public String deleteOperTime(@AuthenticationPrincipal CustomManager manager, @PathVariable int bd_num) {
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteOperTime(bd_num)) {
        return "redirect:/manager/opertimelist/"+rtNum;
    }
		return "redirect:/manager/opertimelist/"+rtNum;
	}

	//매장 편의시설 목록 페이지
	@GetMapping("/resfacilitylist/{rt_num}")
	public String facilityListPage(Model model, @PathVariable int rt_num, @AuthenticationPrincipal CustomManager manager) {
		System.out.println("resfacilitylist rt_num: " + rt_num);
		List<FacilityVO> facility = managerService.getFacilityList();
		List<RestaurantFacilityVO> facilitylist = managerService.getResFacilityList(rt_num);

		System.out.println("등록된 편의 시설 : "+facility);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("facility", facility);
		model.addAttribute("facilitylist", facilitylist);
		model.addAttribute("manager", manager.getManager());
		return "manager/resfacilitylist";
	}

	//매장 편의시설 등록 페이지
	@GetMapping("/make_resfacility")
	public String makeResFacilityPage(Model model) {
		List<FacilityVO> facility = managerService.getFacilityList();

		model.addAttribute("facility", facility);
		model.addAttribute("url", "/make_resfacility");
		return "/manager/make_resfacility";
	}

	@PostMapping("/make_resfacility")
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
			return "redirect:/manager/resfacilitylist/"+rtNum;
		}

		return "/manager/make_resfacility";
	}

	//편의시설 정보 변경
	@GetMapping("/remake_resfacility/{rf_num}")
	public String reMakeResFacilityPage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int rf_num) {
		RestaurantFacilityVO resfacility = managerService.getResFacility(rf_num);
		List<FacilityVO> facility = managerService.getFacilityList();
		System.out.println(resfacility);
		
		model.addAttribute("facility", facility);
		model.addAttribute("resfacility", resfacility);
		model.addAttribute("url", "/remake_resfacility");
		return "/manager/remake_resfacility";
	}
	
	@PostMapping("/remake_resfacility")
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
		 	return "redirect:/manager/resfacilitylist/"+rtNum;
		 }
		return "/manager/remake_resfacility";
	}

	@PostMapping("/delete_resfacility/{rf_num}")
	public String deleteResFacility(@AuthenticationPrincipal CustomManager manager, @PathVariable int rf_num) {
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteResFacility(rf_num)) {
        return "redirect:/manager/resfacilitylist/"+rtNum;
    }
		return "redirect:/manager/resfacilitylist/"+rtNum;
	}


}
