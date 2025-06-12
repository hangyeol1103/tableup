package kr.kh.tableup.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.kh.tableup.model.DTO.DefaultResTimeListDTO;
import kr.kh.tableup.model.util.CustomManager;
import kr.kh.tableup.model.vo.BusinessDateVO;
import kr.kh.tableup.model.vo.BusinessHourTemplateVO;
import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.DefaultResTimeVO;
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

	
	//매니저 메인 페이지
	@GetMapping({"", "/"})
	public String manager(Model model, @AuthenticationPrincipal CustomManager manager) {
		// System.out.println("접속한 계정 : "+manager.getManager());
		// model.addAttribute("url","/main");
		if (manager == null || manager.getManager() == null) {
      return "redirect:/manager/login";
    }
		int rt_num =manager.getManager().getRm_rt_num();
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("manager", manager);
		return "manager/main";
	}

	// 매지저 예약 관리 페이지
	@GetMapping("/restime/restimepage")
	public String reservationPage(Model model, @AuthenticationPrincipal CustomManager manager) {
		if (manager == null || manager.getManager() == null) {
      return "redirect:/manager/login";
    }

		int rt_num =manager.getManager().getRm_rt_num();
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("manager", manager);
		return "manager/restime/restimepage";
	}

	// 매지저 영업일자 관리 페이지
	@GetMapping("/opertime/opertimepage")
	public String opertimePage(Model model, @AuthenticationPrincipal CustomManager manager) {
		if (manager == null || manager.getManager() == null) {
			return "redirect:/manager/login";
		}

		int rt_num =manager.getManager().getRm_rt_num();
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		List<BusinessDateVO> opertime = managerService.getOperTimeList(rt_num);
		

		model.addAttribute("opertime", opertime);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("manager", manager);
		return "manager/opertime/opertimepage";
	}
	
	

	@GetMapping("/login")
	public String manager_login(Model model) {
		model.addAttribute("url", "/login");
		return "manager/login";
	}


	//매니저 회원가입
	@GetMapping("/signup")
	public String manager_signup(Model model) {
		List<RestaurantManagerVO> managerList = managerService.getManagerList();
		
		model.addAttribute("managerList", managerList);
		model.addAttribute("url", "/signup");
		return "/manager/signup";
	}
	
	@PostMapping("/signup")
	public String postMethodName(RestaurantManagerVO rm, RedirectAttributes ra) {
		String signUp = managerService.insertManager(rm);
		if(signUp == null){
			ra.addFlashAttribute("signupError","회원가입에 완료했습니다.");
			return "redirect:/manager/login";
		}
		System.out.println(signUp);
		ra.addFlashAttribute("signupError",signUp);
		return "redirect:/manager/signup";
	}

	//회원가입시 아이디 중복 체크
	@GetMapping("/check/id")
	@ResponseBody
	public boolean checkId(@RequestParam("id") String id) {
		System.out.println("가져온 아이디 : " + id);
		return managerService.getRestaurantById(id); // true: 중복, false: 사용 가능
	}
	//회원가입시 사업자 번호 중복 체크
	@GetMapping("/check/business")
	@ResponseBody
	public boolean checkBusiness(@RequestParam("business") String business) {
		System.out.println("가져온 사업자 번호 : " + business);
		return managerService.getRestaurantByBusiness(business); // true: 중복, false: 사용 가능
	}

	//아이디 찾기(이름, 전화번호, 사업자 번호 확인)
	@GetMapping("/findId")
	public String findManagerId(Model model) {
		List<RestaurantManagerVO> managerList = managerService.getManagerList();		
		model.addAttribute("managerList", managerList);
		return "manager/findId";
	}

	@PostMapping("/findId")
	public String findManagerIdInfo(@RequestParam String rm_name, @RequestParam String rm_phone, @RequestParam String rm_business, Model model) {
		System.out.println("rm_name : "+rm_name);
		System.out.println("rm_phone : "+rm_phone);
		System.out.println("rm_business : "+rm_business);
		RestaurantManagerVO manager = managerService.findManager(rm_name, rm_phone, rm_business);
		
		if (manager == null) {
			System.out.println("아직 회원 가입한 계정이 아닙니다. ");
			model.addAttribute("error", "아직 회원 가입한 계정이 아닙니다.");
			return "manager/findId"; 
		}

		System.out.println("아이디 : " + manager.getRm_id());
		model.addAttribute("foundId", manager.getRm_id());
		model.addAttribute("rm_num", manager.getRm_num()); // 비번 찾기 연동용
		return "manager/showIdResult"; // <- 결과 페이지로 이동

	}

	// 아이디 찾기 결과 페이지
	@GetMapping("/showIdResult")
	public String findManagerIdShow(@RequestParam int rm_num, @RequestParam int foundId, Model model) {
		
		if (rm_num == 0) {
      return "redirect:/manager/login";
    }

		model.addAttribute("foundId", foundId);
		model.addAttribute("rm_num", rm_num);
		return "manager/showIdResult";
	}
	
	
	

	//비밀번호 재설정(아이디, 이메일 확인)
	@GetMapping("/findPw")
	public String findManagerPw(Model model) {
		List<RestaurantManagerVO> managerList = managerService.getManagerList();
		System.out.println("등록된 매니저 계정 : " + managerList);		
		model.addAttribute("managerList", managerList);
		return "manager/findPw";
	}

	// 1. 아이디 + 이메일 확인
	@PostMapping("/findPw")
	public String checkManagerInfo(@RequestParam String rm_id, @RequestParam String rm_email, Model model) {
		System.out.println(rm_id);
		System.out.println(rm_email);
		RestaurantManagerVO manager = managerService.findIdAndEmail(rm_id, rm_email);
		if (manager == null) {
			System.out.println("아이디 또는 이메일이 일치하지 않습니다.");
			model.addAttribute("error", "아이디 또는 이메일이 일치하지 않습니다.");
			return "manager/findPw"; // 여기만 변경됨!
		}
		System.out.println("rm_num : "+manager.getRm_num());
		System.out.println("변경할 계정 : "+manager);
		model.addAttribute("rm_num", manager.getRm_num());
		model.addAttribute("manager", manager);
		return "redirect:/manager/updatePw?rm_num=" + manager.getRm_num(); // 비번 재설정 화면으로 이동
	}

	//비밀번호 재설정(새 비밀번호 입력)
	@GetMapping("/updatePw")
	public String updateManagerPw(@RequestParam int rm_num, Model model) {
		System.out.println("변경할 매니저의 기본키 : " + rm_num);
		
		if (rm_num == 0) {
      return "redirect:/manager/login";
    }

		model.addAttribute("rm_num", rm_num);
		return "manager/updatePw"; 
	}

	@PostMapping("/updatePw")
	public String updateManagerPwInfo(@RequestParam int rm_num, @RequestParam String rm_pw, @RequestParam String confirmPw,
    								  RedirectAttributes redirectAttributes) {
		System.out.println("변경할 매니저의 기본키 : " + rm_num);
		System.out.println("변경할 매니저의 비번 : " + rm_pw);
		System.out.println("비밀번호 확인 : " + confirmPw);
		// 비밀번호 불일치 체크
		if (!rm_pw.equals(confirmPw)) {
			redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
			return "redirect:/manager/updatePw?rm_num=" + rm_num;
		}

		RestaurantManagerVO manager =managerService.getManager(rm_num);
		manager.setRm_pw(rm_pw);
		boolean res = managerService.updateManagerPW(manager);
		if(!res){
			System.out.println("변경 실패했습니다.");
			redirectAttributes.addFlashAttribute("error", "비밀번호 변경에 실패했습니다.");
			return "redirect:/manager/updatePw?rm_num=" + rm_num;
		}
		return "redirect:/manager/login";
	}
	
	
	//매장 페이지
	@GetMapping("/restaurant/restaurant/{rm_id}")
	public String restaurantPage(@PathVariable("rm_id") String rm_id,Model model, @AuthenticationPrincipal CustomManager pManager /*Principal principal*/) {
		// String loginId = principal.getName();
		String loginId = pManager.getManager().getRm_id();
		RestaurantManagerVO manager = managerService.getManagerId(loginId);
		System.out.println("접속한 아이디 : " + pManager);
		System.out.println("가져올 매니저 객체 : " + manager);
		System.out.println("매니저 아이디 : " + rm_id);
		if (!loginId.equals(rm_id)|| manager == null) {
			if(manager != null){
				System.out.println("다른 계정의 매장 접근 X");
				return "redirect:/manager";
			}
			System.out.println("매니저 회원 가입 필요!");
			return "redirect:/manager/login";
		}


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
    public String redirectRestaurant(@AuthenticationPrincipal CustomManager pManager) {
      // String loginId = principal.getName();  
			String loginId = pManager.getManager().getRm_id();
      return "redirect:/manager/restaurant/restaurant/" + loginId;
    }

	@GetMapping("/restaurant/make")
	public String makePage(Model model, @AuthenticationPrincipal CustomManager manager) {
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
      return "redirect:/manager/login";
    }
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
		
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }
		System.out.println(manager);
		System.out.println(restaurant);

		if(managerService.insertRestaurant(restaurant, manager.getManager(), fileList)){
			return "redirect:/manager/detail/restaurantdetail";		//이거를 디테일화면으로 넘어가게
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
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
      return "redirect:/manager/login";
    }

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
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }
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

		//매니저 정보가 없는 경우
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/login";
		}

		if(rt_num != manager.getManager().getRm_rt_num()){
			return "redirect:/manager";
		}
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);

		System.out.println("menulist rt_num: " + rt_num);
		List<MenuVO> menulist = managerService.getMenuList(rt_num);
		List<MenuTypeVO> menutype = managerService.getMenuTypeList();
		
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("menulist", menulist);
		model.addAttribute("menutype", menutype);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		System.out.println(manager.getManager());
		return "manager/menu/menulist";
	}

	//메뉴 등록 페이지
	@GetMapping("/menu/make_menu")
	public String makeMenuPage(Model model, @AuthenticationPrincipal CustomManager manager) {
		
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/login";
		}

		if (manager.getManager().getRm_rt_num() <= 0) {
			// 매장 정보가 없는 매니저 → 매장 등록 페이지로
			System.out.println("매니저 정보가 없습니다.");
			return "redirect:/manager/restaurant/make";
    }
		List<MenuTypeVO> menutype = managerService.getMenuTypeList();
		int rt_num =manager.getManager().getRm_rt_num();
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);

		model.addAttribute("restaurant", restaurant);
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

		//매니저 정보가 없는 경우
		if (manager == null|| manager.getManager() == null ) {
        return "redirect:/manager/login";
    }
		
		int rtNum = manager.getManager().getRm_rt_num();
    System.out.println("매니저의 매장 번호: " + rtNum);
    menu.setMn_rt_num(rtNum);

    if (rtNum <= 0) {
        // 매장 정보가 없는 매니저 → 매장 등록 페이지로
				System.out.println("매니저 정보가 없습니다.");
        return "redirect:/manager/restaurant/make";
    }

		if(managerService.makeMenu(menu,mn_img2)){
			return "redirect:/manager/menu/menulist/"+rtNum;
		}

		// return "/manager/menu/make_menu/";
		return "redirect:/manager/menu/menulist/"+rtNum;
	}
	
	//메뉴 상세 페이지
	@GetMapping("/menu/menu/{mn_num}")
	public String detailMenu(Model model, @PathVariable int mn_num, @AuthenticationPrincipal CustomManager manager) {
		
		//매니저 정보가 없는 경우
		if (manager == null|| manager.getManager() == null) {
        return "redirect:/manager/login";
    }

		MenuVO menu = managerService.getMenu(mn_num);
		MenuTypeVO menutype =managerService.getMenuType(menu.getMn_mt_num());
		
		//다른 계정의 매니저가 접근했을 경우
		if(manager.getManager().getRm_rt_num() != menu.getMn_rt_num()){
			return "redirect:/manager";
		}

		int rt_num =manager.getManager().getRm_rt_num();
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		
		List<MenuTypeVO> menutypeList = managerService.getMenuTypeList();
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("menu", menu);
		model.addAttribute("menutypeList", menutypeList);
		model.addAttribute("menutype", menutype);
		return "/manager/menu/menu";
	}

	//메뉴 삭제
	@PostMapping("/menu/delete_menu/{mn_num}")
	public String deleteMenuPage(@AuthenticationPrincipal CustomManager manager, @PathVariable int mn_num) {
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteMenu(mn_num)) {
        return "redirect:/manager/menu/menulist/"+rtNum;
    }
		return "redirect:/manager/menu/menu?mn_num=" + mn_num;
	}
	

	//메뉴 수정 페이지
	@GetMapping("/menu/remake_menu")
	public String reMakeMenuPage(Model model, @AuthenticationPrincipal CustomManager manager, @RequestParam("mn_num") int mn_num) {
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }

		List<MenuTypeVO> menutype = managerService.getMenuTypeList();
		MenuVO menu = managerService.getMenu(mn_num);
		System.out.println(menu);

		int rt_num =manager.getManager().getRm_rt_num();
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		model.addAttribute("restaurant", restaurant);

		

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("url", "/remake_menu");
		model.addAttribute("menutype", menutype);
		model.addAttribute("menu", menu);
		return "/manager/menu/remake_menu";
	}

	@PostMapping("/menu/remake_menu")
	@ResponseBody
	public Map<String, Object> updateMenu(MenuVO menu, MultipartFile mn_img2, @AuthenticationPrincipal CustomManager manager) {
		Map<String, Object> result = new HashMap<>();

		int rtNum = manager.getManager().getRm_rt_num();
		menu.setMn_rt_num(rtNum);

		if(manager==null || manager.getManager()==null){
			result.put("success", false);
			result.put("redirect", "/manager/login");
			return result;
		}

		if (rtNum <= 0) {
			result.put("success", false);
			result.put("redirect", "/manager/restaurant/make");
			return result;
		}

		boolean success = managerService.updateMenu(menu, mn_img2);
		result.put("success", success);
		result.put("redirect", "/manager/menu/menulist/" + rtNum);
		return result;
	}

	
	//매장 상세 정보 출력
	@GetMapping("/detail/restaurantdetail")
	public String restaurantDetailPage(Model model, @AuthenticationPrincipal CustomManager manager) {
		
		//매니저 정보가 없는 경우
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/login";
		}
		int rt_num = manager.getManager().getRm_rt_num();

		if(rt_num != manager.getManager().getRm_rt_num() || rt_num == 0){
			return "redirect:/manager";
		}

		
		RestaurantVO resdetail = managerService.getResDetail(rt_num);
		List<DefaultResTimeVO> defaultTimeList = managerService.getDefaultTimeList(rt_num);

		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		model.addAttribute("restaurant", restaurant);
		if(resdetail != null){
			model.addAttribute("resdetail", resdetail);
			model.addAttribute("defaultTimeList", defaultTimeList);
		}
		model.addAttribute("manager", manager.getManager());
		return "/manager/detail/restaurantdetail";
	}

	@GetMapping("/detail/make_detail")
	public String makeResDetailPage(Model model,@AuthenticationPrincipal CustomManager manager) {
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null ) {
        return "redirect:/manager/login";
    }
		int rt_num =manager.getManager().getRm_rt_num();
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("url", "/make_detail");
		return "/manager/detail/make_detail";
	}

	
	// @PostMapping("/detail/make_detail")
	// public String insertResDetailPage(RestaurantVO resdetail, @AuthenticationPrincipal CustomManager manager,@ModelAttribute("drtList") List<DefaultResTimeVO> drtList ) {
	// 	int rtNum = manager.getManager().getRm_rt_num();
  //   //매니저 정보가 없는 경우
	// 	if (manager == null || manager.getManager() == null || rtNum == 0) {
  //       return "redirect:/manager/login";
  //   }
	// 	System.out.println("매니저의 정보: " + manager);
	// 	System.out.println("매니저의 매장 번호: " + rtNum);
  //   resdetail.setRt_num(rtNum);
	// 	resdetail.setRd_rt_num(rtNum);
		
	// 	System.out.println(manager);
	// 	System.out.println(resdetail);
	// 	for(DefaultResTimeVO drt : drtList){
	// 		drt.setDrt_rt_num(rtNum);
	// 		System.out.println("영업시간 정보 : " + drt);
	// 	}
	// 	System.out.println("등록된 영업일자 목록들 : " + drtList);
	// 	System.out.println("등록된 영업일자 목록의 길이 : "+ drtList.size());

	// 	if(drtList.size()!=0){
	// 		managerService.insertDefaultResTimeList(drtList, rtNum);
	// 	}

  //   if (rtNum <= 0) {
  //       // 매장 정보가 없는 매니저 → 매장 등록 페이지로
  //       return "redirect:/manager/make";
  //   }
	// 	if(managerService.insertResDetail(resdetail)){
			
	// 		// return "redirect:/manager/detail/restaurantdetail/"+ rtNum;
	// 		return "redirect:/manager/detail/restaurantdetail";
	// 	}
	// 	return "redirect:/manager/detail/make_detail";
	// }

	// @PostMapping("/detail/make_detail2")
	// public String insertResDetailPage2(
	// 		RestaurantVO resdetail,
	// 		@AuthenticationPrincipal CustomManager manager,
	// 		@RequestParam(value = "drtJson", required = false) String drtJson) {

	// 		int rtNum = manager.getManager().getRm_rt_num();
	// 		if (manager == null || rtNum == 0) return "redirect:/manager/login";

	// 		resdetail.setRt_num(rtNum);
	// 		resdetail.setRd_rt_num(rtNum);
	// 		System.out.println("--------------");
	// 		System.out.println("drtJson : " + drtJson);

	// 		if (drtJson != null && !drtJson.trim().isEmpty()) {
	// 				try {
	// 						ObjectMapper mapper = new ObjectMapper();
	// 						List<DefaultResTimeVO> drtList = mapper.readValue(drtJson, new TypeReference<List<DefaultResTimeVO>>() {});
	// 						for (DefaultResTimeVO drt : drtList) {
	// 								drt.setDrt_rt_num(rtNum);
	// 						}
	// 						System.out.println("입력받은 영업일자 리스트 : " + drtList);
	// 						managerService.insertDefaultResTimeList(drtList, rtNum);
	// 				} catch (Exception e) {
	// 						e.printStackTrace();
	// 						// 에러 발생 시 실패 처리
	// 						return "redirect:/manager/detail/make_detail";
	// 				}
	// 		}

	// 		System.out.println("--------------");
	// 		System.out.println("저장할 상세정보 : " + resdetail);
	// 		if (managerService.insertResDetail(resdetail)) {
	// 				return "redirect:/manager/detail/restaurantdetail";
	// 		}

	// 		return "redirect:/manager/detail/make_detail";
	// }

	@PostMapping("/detail/make_detail")
	public String insertResDetailPage(
			RestaurantVO resdetail,
			@AuthenticationPrincipal CustomManager manager,
			DefaultResTimeListDTO drtDTO) {
			int rtNum = manager.getManager().getRm_rt_num();
			if (manager == null || rtNum == 0) return "redirect:/manager/login";

			resdetail.setRt_num(rtNum);
			resdetail.setRd_rt_num(rtNum);
			
			System.out.println("받은 영업일자 : " + drtDTO);
			System.out.println("저장할 상세정보 : " + resdetail);

			List<DefaultResTimeVO> list = drtDTO.getList();
			if (list != null) {
					for (DefaultResTimeVO drt : list) {
							drt.setDrt_rt_num(rtNum); // 매장 번호 설정
					}
			}
			// 영업시간 리스트 저장
			if (list != null && !list.isEmpty()) {
					managerService.insertDefaultResTimeList(list, rtNum);
			}

			if (managerService.insertResDetail(resdetail)) {
					return "redirect:/manager/detail/restaurantdetail";
			}

			return "redirect:/manager/detail/make_detail";
	}

	//상세정보 변경 페이지
	@GetMapping("/detail/remake_detail")
	public String reMakeDetailPage(Model model, @AuthenticationPrincipal CustomManager manager) {

		// 매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
      return "redirect:/manager/login";
    }
		
		int rtNum = manager.getManager().getRm_rt_num();
		System.out.println("매니저의 매장 번호: " + rtNum);
		RestaurantVO resdetail = managerService.getResDetail(rtNum);
		List<DefaultResTimeVO> defaultTimeList = managerService.getDefaultTimeList(rtNum);

		System.out.println("defaultTimeList : "+defaultTimeList);
		int rt_num =manager.getManager().getRm_rt_num();
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		System.out.println(resdetail);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("res", resdetail);	
		model.addAttribute("defaultTimeList", defaultTimeList);
		model.addAttribute("url", "/remake_detail");
		return "/manager/detail/remake_detail";
	}
	
	// @PostMapping("/detail/remake_detail")
	// public String updateDetail(RestaurantVO resdetail, @AuthenticationPrincipal CustomManager manager) {
	// 	//매니저 정보가 없는 경우
	// 	if (manager == null || manager.getManager() == null) {
  //       return "redirect:/manager/login";
  //   }

	// 	int rtNum = manager.getManager().getRm_rt_num();
  //   System.out.println("매니저의 매장 번호: " + rtNum);
  //   resdetail.setRd_rt_num(rtNum);
		
	// 	System.out.println(manager);
	// 	System.out.println(resdetail);

  //   if (rtNum <= 0) {
  //       // 매장 정보가 없는 매니저 → 매장 등록 페이지로
  //       return "redirect:/manager/make";
  //   }

	// 	if(managerService.updateDetail(resdetail)){
	// 		// return "redirect:/manager/detail/restaurantdetail/"+ rtNum;
	// 		return "redirect:/manager/detail/restaurantdetail";
	// 	}
	// 	System.out.println("수정 실패");
	// 	return "/manager/detail/remake_detail";
	// }

	@PostMapping("/detail/remake_detail")
	public String updateDetail(RestaurantVO resdetail, @AuthenticationPrincipal CustomManager manager, DefaultResTimeListDTO drtDTO) {
			int rtNum = manager.getManager().getRm_rt_num();
			if (manager == null || rtNum == 0){
				return "redirect:/manager/login";
			}
			if (rtNum <= 0) {
         // 매장 정보가 없는 매니저 → 매장 등록 페이지로
         return "redirect:/manager/make";
     	} 

			resdetail.setRt_num(rtNum);
			resdetail.setRd_rt_num(rtNum);
			
			System.out.println("수정할 영업일자 : " + drtDTO);
			System.out.println("수정할 상세정보 : " + resdetail);

			List<DefaultResTimeVO> list = drtDTO.getList();
			if (list != null) {
					for (DefaultResTimeVO drt : list) {
							drt.setDrt_rt_num(rtNum); // 매장 번호 설정
					}
			}
			// 영업시간 변경 
			if (list != null && !list.isEmpty()) {
					managerService.updateDefaultResTimeList(list, rtNum);
			}

			if(managerService.updateDetail(resdetail)){
				return "redirect:/manager/detail/restaurantdetail";
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
			return "redirect:/manager";
		}


		System.out.println("couponlist rt_num: " + rt_num);
		List<ResCouponVO> couponlist = managerService.getCouponList(rt_num);
		
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("couponlist", couponlist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		return "manager/coupon/couponlist";
	}

	//쿠폰 등록 페이지
	@GetMapping("/coupon/make_coupon")
	public String makeCouponPage(Model model, @AuthenticationPrincipal CustomManager manager) {
		// 매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
      return "redirect:/manager/login";
    }
		int rt_num =manager.getManager().getRm_rt_num();
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("url", "/make_coupon");
		return "/manager/coupon/make_coupon";
	}

	@PostMapping("/coupon/make_coupon")
	@ResponseBody
	public boolean insertCoupon(@RequestBody ResCouponVO coupon, @AuthenticationPrincipal CustomManager manager) {
		//매니저 정보가 없거나, 쿠폰 객체가 없는 경우
		if (coupon == null || manager == null || manager.getManager() == null){
			return false;
		} 
		int rtNum = manager.getManager().getRm_rt_num();
		coupon.setRec_rt_num(rtNum);
		return managerService.makeCoupon(coupon);
	}

	//쿠폰 정보 출력 페이지
	@GetMapping("/coupon/coupon/{rec_num}")
	public String detailCoupon(Model model, @PathVariable int rec_num, @AuthenticationPrincipal CustomManager manager) {
		// 매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
      return "redirect:/manager/login";
    }
		int rt_num =manager.getManager().getRm_rt_num();
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		model.addAttribute("restaurant", restaurant);

		ResCouponVO coupon = managerService.getCoupon(rec_num);

		//다른 계정의 매니저가 접근했을 경우
		if(manager.getManager().getRm_rt_num() != coupon.getRec_rt_num()){
			return "redirect:/manager";
		}

		model.addAttribute("coupon", coupon);
		return "/manager/coupon/coupon";
	}

	//쿠폰 수정 페이지
	@GetMapping("/coupon/remake_coupon/{rec_num}")
	public String reMakeCouponPage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int rec_num) {
		// 매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
      return "redirect:/manager/login";
    }

		ResCouponVO coupon = managerService.getCoupon(rec_num);
    System.out.println(coupon);
		
		int rt_num =manager.getManager().getRm_rt_num();
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("coupon", coupon);
		model.addAttribute("url", "/remake_coupon");
		return "/manager/coupon/remake_coupon";
	}
	
	@PostMapping("/coupon/remake_coupon")
	@ResponseBody
	public boolean updateCouponAjax(@RequestBody ResCouponVO coupon, @AuthenticationPrincipal CustomManager manager) {
		int rtNum = manager.getManager().getRm_rt_num();
		if (rtNum <= 0) return false;

		coupon.setRec_rt_num(rtNum);

		return managerService.updateCoupon(coupon);
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


		//매니저 정보가 없는 경우
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/";
		}

		if(rt_num != manager.getManager().getRm_rt_num()){
			return "redirect:/manager";
		}

		System.out.println("newslist rt_num: " + rt_num);
		List<ResNewsVO> newslist = managerService.getNewsList(rt_num);

		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("newslist", newslist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		return "manager/news/newslist";
	}
	@GetMapping("/news/make_news")
	public String makeNewsPage(Model model, @AuthenticationPrincipal CustomManager manager) {

		// 매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
      return "redirect:/manager/login";
    }

		model.addAttribute("url", "/make_news");
		return "/manager/news/make_news";
	}

	@PostMapping("/news/make_news")
	public String insertCoupon(ResNewsVO news,  @AuthenticationPrincipal CustomManager manager) {
		// 매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
      return "redirect:/manager/login";
    }

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
	public String detailNews(Model model, @PathVariable int rn_num, @AuthenticationPrincipal CustomManager manager) {
		// 매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
      return "redirect:/manager/login";
    }
		
		int rt_num =manager.getManager().getRm_rt_num();
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		model.addAttribute("restaurant", restaurant);

		ResNewsVO news = managerService.getNews(rn_num);
		System.out.println(news);
		//다른 계정의 매니저가 접근했을 경우
		if(manager.getManager().getRm_rt_num() != news.getRn_rt_num()){
			return "redirect:/manager";
		}
		model.addAttribute("news", news);
		return "/manager/news/news";
	}

	//소식 수정 페이지
	@GetMapping("/news/remake_news/{rn_num}")
	public String reMakeNewsPage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int rn_num) {
		if (manager == null || manager.getManager() == null) {
      return "redirect:/manager/login";
    }

		ResNewsVO news = managerService.getNews(rn_num);
    System.out.println(news);
		
		int rt_num =manager.getManager().getRm_rt_num();
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		model.addAttribute("restaurant", restaurant);

		model.addAttribute("news", news);
		model.addAttribute("url", "/news/remake_news");
		return "/manager/news/remake_news";
	}
	
	@PostMapping("/news/remake_news")
	@ResponseBody
	public boolean updateNews(ResNewsVO news,  @AuthenticationPrincipal CustomManager manager) {
		int rtNum = manager.getManager().getRm_rt_num();

		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null || rtNum == 0) {
        return false;
    }

		if (rtNum <= 0) {
			System.out.println("매장 정보 없음, 매장 등록이 필요합니다.");
			return false;
		}

		news.setRn_rt_num(rtNum);
		System.out.println("수정 요청 받은 내용: " + news);

		return managerService.updateNews(news); // true/false 반환
	}

	//소식 삭제
	@PostMapping("/news/delete_news/{rn_num}")
	public String deleteNewsPage(@AuthenticationPrincipal CustomManager manager, @PathVariable int rn_num) {
		int rtNum = manager.getManager().getRm_rt_num();
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null || rtNum == 0) {
        return "redirect:/manager/login";
    }
		 if(managerService.deleteNews(rn_num)) {
        return "redirect:/manager/news/newslist/"+rtNum;
    }
		return "redirect:/manager/news/news?rec_num=" + rn_num;
	}


	//예약 가능 시간 페이지
	//예약 가능 시간 목록(리스트)
	@GetMapping("/restime/restimelist/{rt_num}")
	public String ResTimeListPage(Model model, @PathVariable int rt_num, @AuthenticationPrincipal CustomManager manager) {		
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/login";
		}

		if(rt_num != manager.getManager().getRm_rt_num()){
			return "redirect:/manager";
		}

		System.out.println("restimelist rt_num: " + rt_num);

		List<BusinessHourVO> restimelist = managerService.getResTimeList(rt_num);
		List<BusinessDateVO> opertimelist= managerService.getOperTimeList(rt_num);
		List<BusinessHourTemplateVO> templateList =managerService.getTemplateList(rt_num);


		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("opertimelist", opertimelist);
		model.addAttribute("restimelist", restimelist);
		model.addAttribute("templateList", templateList);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		return "manager/restime/restimelist";
	}

	//예약 가능 시간 목록 JSON 반환 용
	@GetMapping("/restime/restimelist_json/{rt_num}")
	public List<BusinessHourVO> getResTimeListJson(@PathVariable int rt_num, @AuthenticationPrincipal CustomManager manager) {
		if(manager == null||manager.getManager() == null || manager.getManager().getRm_rt_num() != rt_num){
			return Collections.emptyList();
		}
		return managerService.getResTimeList(rt_num);
	}
	

	//예약 시간 등록 페이지
	@GetMapping("/restime/make_restime")
	public String makeResTimePage(Model model, @AuthenticationPrincipal CustomManager manager) {
		int rt_num = manager.getManager().getRm_rt_num();
		List<BusinessDateVO> opertimelist= managerService.getOperTimeList(rt_num);
		List<BusinessHourVO> restimelist = managerService.getResTimeList(rt_num);
		List<BusinessHourTemplateVO> templateList =managerService.getTemplateList(rt_num);

		model.addAttribute("manager", manager.getManager());
		model.addAttribute("restimelist", restimelist);
		model.addAttribute("opertimelist", opertimelist);
		model.addAttribute("templateList", templateList);
		model.addAttribute("url", "/make_restime");
		return "/manager/restime/make_restime";
	}

	@GetMapping("/make_restime_sub") 
	public String makeResTimeSubPage(Model model,  @AuthenticationPrincipal CustomManager manager) {
		if(manager.getManager().getRm_id() == null) {
			return "/manager/login";
		}
		model.addAttribute("managerId", manager.getManager().getRm_id());
		model.addAttribute("url", "/make_restime_sub");
		return "/manager/restime/make_restime_sub";
	}

	@PostMapping("/restime/make_restime_list")
	@ResponseBody
	public String insertResTimeList(@RequestBody Map<String, Object> payload,
																	@AuthenticationPrincipal CustomManager manager) {

			if (manager == null || manager.getManager() == null) {
					return "로그인 정보가 없습니다.";
			}

			boolean overwrite = Boolean.parseBoolean(payload.get("overwrite").toString());
			List<LinkedHashMap<String, Object>> rawList = (List<LinkedHashMap<String, Object>>) payload.get("list");

			if (rawList == null || rawList.isEmpty()) {
					return "등록할 예약 일자가 없습니다.";
			}

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			List<BusinessHourVO> resList = new ArrayList<>();
			for (Map<String, Object> map : rawList) {
					BusinessHourVO vo = new BusinessHourVO();

					vo.setBh_date((String) map.get("bh_date"));

					String bhStartStr = (String) map.get("bh_start");
					String bhEndStr = (String) map.get("bh_end");

					if (bhStartStr != null && !bhStartStr.isBlank()) {
							vo.setBh_start(LocalDateTime.parse(bhStartStr, formatter));
					}
					if (bhEndStr != null && !bhEndStr.isBlank()) {
							vo.setBh_end(LocalDateTime.parse(bhEndStr, formatter));
					}

					vo.setBh_seat_max((int) map.get("bh_seat_max"));
					vo.setBh_table_max((int) map.get("bh_table_max"));
					vo.setBh_rt_num((int) map.get("bh_rt_num"));

					resList.add(vo);
			}

			System.out.println("resList : " + resList);

			List<BusinessDateVO> opertimeList = managerService.getOperTimeList(manager.getManager().getRm_rt_num());

			Set<String> businessDateSet = opertimeList.stream()
							.map(bd -> bd.getBd_date().substring(0, 10))
							.collect(Collectors.toSet());

			boolean hasInvalidDate = resList.stream()
							.map(rt -> rt.getBh_start().toLocalDate().toString())
							.anyMatch(dateStr -> !businessDateSet.contains(dateStr));

			if (hasInvalidDate) {
					return "영업일자가 등록되지 않은 날짜가 포함되어 있습니다.";
			}

			Map<String, BusinessDateVO> operDateMap = opertimeList.stream()
							.collect(Collectors.toMap(bd -> bd.getBd_date().substring(0, 10), bd -> bd));

			int rtNum = manager.getManager().getRm_rt_num();
			int success = 0, fail = 0;

			for (BusinessHourVO restime : resList) {
					restime.setBh_rt_num(rtNum);

					String dateKey = restime.getBh_date();
					if (dateKey == null || dateKey.isBlank()) {
							continue;
					}

					System.out.println("------------------");
					System.out.println("overwrite : " + overwrite);
					System.out.println("dateKey : " + dateKey);
					System.out.println("------------------");

					if (overwrite) {
							System.out.println("덮어쓰기: 기존 데이터 삭제 시도 " + dateKey);
							managerService.deleteRestimesByDate(rtNum, restime.getBh_start(), restime.getBh_end());
					}

					if (!overwrite) {
							boolean exists = managerService.existsRestime(rtNum, restime.getBh_start(), restime.getBh_date());
							if (exists) continue;
					}

					if (managerService.makeResTiem(restime, operDateMap)) success++;
					else fail++;
			}

			return "등록 성공 " + success + "건 / 실패 " + fail + "건";
	}


	//예약 시간 변경
	@GetMapping("/restime/remake_restime/{bh_num}")
	public String reMakeResTimePage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int bh_num) {
		if (manager == null || manager.getManager() == null) {
      return "redirect:/manager/login";
    }

		BusinessHourVO restime = managerService.getBusinessHour(bh_num);
    System.out.println(restime);
		
		

		model.addAttribute("restime", restime);
		model.addAttribute("url", "/remake_restime");
		return "/manager/restime/remake_restime";
	}

	@PostMapping("/restime/remake_restime")
	@ResponseBody
	public ResponseEntity<?> updateRestime(@RequestBody BusinessHourVO restime,  @AuthenticationPrincipal CustomManager manager ) {
			
			if (manager == null || manager.getManager() == null) {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 없습니다.");
			}

			restime.setBh_rt_num(manager.getManager().getRm_rt_num());
			System.out.println("받은 시간 : " + restime);

			int rtNum = manager.getManager().getRm_rt_num();
			restime.setBh_rt_num(rtNum);
			

			if (rtNum <= 0) {
					// 매장 정보가 없는 매니저 → 매장 등록 페이지로
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("매장 정보 없음");
			}
			if(managerService.remakeResTime(restime)){
				return ResponseEntity.ok("수정 완료");
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패");
	}
	


	//예약 가능 시간 삭제
	@PostMapping("/restime/delete_restime/{bh_num}")
	public String deleteResTime(@AuthenticationPrincipal CustomManager manager, @PathVariable int bh_num) {		
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }

		int rtNum = manager.getManager().getRm_rt_num();
		if(managerService.deleteResTime(bh_num)) {
      return "redirect:/manager/restime/restimelist/"+rtNum;
    }
		return "redirect:/manager/restime/restimelist/"+rtNum;
	}

	//요일에 저장된 예약 가능 시간 전체 삭제
	@PostMapping("/restime/delete_by_date")
	@ResponseBody
	public boolean deleteByDate(@RequestBody Map<String, Object> data) {
		int rt_num = Integer.parseInt(data.get("rt_num").toString());
    String date = (String) data.get("date");
    if (rt_num == 0 || date == null){ 
			return false;
		}
    return managerService.deleteAllRestimes(rt_num, date);
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
			return "redirect:/manager/login";
		}

		if(rt_num != manager.getManager().getRm_rt_num()){
			return "redirect:/manager";
		}


		System.out.println("opertimelist rt_num: " + rt_num);
		List<BusinessDateVO> opertimelist = managerService.getOperTimeList(rt_num);
		System.out.println(opertimelist);
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("opertimelist", opertimelist);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("manager", manager.getManager());
		return "manager/opertime/opertimelist";
	}

	//영업 일자 등록 페이지
	@GetMapping("/opertime/make_opertime")
	public String makeOperTimePage(Model model, @AuthenticationPrincipal CustomManager manager) {
		if (manager == null || manager.getManager() == null) {
			return "redirect:/manager/login";
		}

		int rt_num = manager.getManager().getRm_rt_num();
		List<BusinessDateVO> opertimelist = new ArrayList<>();
		try {
			opertimelist = managerService.getOperTimeList(rt_num);
		} catch (Exception e) {
			e.printStackTrace(); // 에러 로그 확인
		}

		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("opertimelist", opertimelist);
		model.addAttribute("manager", manager.getManager());
		model.addAttribute("url", "/make_opertime");
		return "/manager/opertime/make_opertime";
	}


	@GetMapping("/make_opertime_sub") 
	public String makeOperTimeSubPage(Model model,  @AuthenticationPrincipal CustomManager manager) {
		if(manager == null || manager.getManager() == null||manager.getManager().getRm_id() == null) {
			return "/manager/login";
		}
		model.addAttribute("managerId", manager.getManager().getRm_id());
		model.addAttribute("url", "/make_opertime_sub");
		return "/manager/opertime/make_opertime_sub";
	}


	@PostMapping("/opertime/make_opertime")
	public String insertOperTime(BusinessDateVO opertime,  @AuthenticationPrincipal CustomManager manager) {
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }

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
			, @AuthenticationPrincipal CustomManager manager
	) {
			// List<BusinessDateVO> operList = new ArrayList<>();
			System.out.println("ajax 수신");
			if (manager == null || manager.getManager() == null) {
					return "로그인 정보가 없습니다.";
			}
			if (operList == null || operList.isEmpty()) {
					return "등록할 영업일자가 없습니다.";
			}
			System.out.println("opertimelist: " + operList);
			

			//int rtNum = 1;
			int rtNum = manager.getManager().getRm_rt_num();
			System.out.println("매니저의 매장 번호: " + rtNum);
			int result = 0;
			for (BusinessDateVO oper : operList) {
					oper.setBd_rt_num(rtNum); 
					if (oper.getBd_date() == null || oper.getBd_open() == null || oper.getBd_close() == null) {
							continue; 
					}

					managerService.makeOperTime(oper); // insert 로직 호출
					result++;
			}

			return "등록 성공 " + result + "건";
	}


	//영업 일자 변경
	@GetMapping("/opertime/remake_opertime/{bd_num}")
	public String reMakeOperTimePage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int bd_num) {

		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }

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
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateOpertime(
			@RequestBody BusinessDateVO opertime,
			@AuthenticationPrincipal CustomManager manager) {

		Map<String, Object> response = new HashMap<>();

		if (manager == null || manager.getManager() == null) {
			response.put("success", false);
			response.put("message", "로그인 정보가 없습니다.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

		int rtNum = manager.getManager().getRm_rt_num();
		if (rtNum <= 0) {
			response.put("success", false);
			response.put("message", "매장 정보가 없습니다.");
			return ResponseEntity.badRequest().body(response);
		}

		opertime.setBd_rt_num(rtNum);
		System.out.println("수정할 영업일자: " + opertime);

		boolean result = managerService.remakeOperTime(opertime);

		if (result) {
			response.put("success", true);
		} else {
			response.put("success", false);
			response.put("message", "영업일자 수정에 실패했습니다.");
		}

		return ResponseEntity.ok(response);
	}

	//영업 시간 삭제
	@PostMapping("/opertime/delete_opertime/{bd_num}")
	@ResponseBody
	public String deleteOperTime(@AuthenticationPrincipal CustomManager manager, @PathVariable int bd_num) {
		//int rtNum = manager.getManager().getRm_rt_num();
		
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }
		System.out.println("삭제할 날짜의 기본키 : " + bd_num);
		if(managerService.deleteOperTime(bd_num)) {
			return "success";
		}
		return "fail";
	}

	//매니저페이지
	@GetMapping("/managerpage")
	public String managerPage(Model model, @AuthenticationPrincipal CustomManager manager) {
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }
		RestaurantManagerVO rm = manager.getManager();
		int rt_num = manager.getManager().getRm_rt_num();
		RestaurantVO restaurant =managerService.getRestaurantByNum(rt_num);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("rm", rm);

		return "/manager/managerpage";
	}

	//입력한 현재 비번하고 db에 등록된 현재 비번하고 비교
	@PostMapping("/checkPassword")
	@ResponseBody
	public boolean checkPassword(@RequestBody Map<String, String> param) {
			String rm_id = param.get("rm_id");
			String inputPw = param.get("rm_pw2");
			
			System.out.println("확인할 아이디 : " + rm_id);
			System.out.println("입력받은 비번 : " + inputPw);

			boolean res = managerService.selectManagerByIdAndCpw(rm_id,inputPw);
			if (!res){
				return false;
			}
			return res;
	}

	@PostMapping("/managerpage")
	public String PostmanagerPage(Model model, @AuthenticationPrincipal CustomManager manager, RestaurantManagerVO rm) {
		//매니저 정보가 없는 경우 
		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }
		RestaurantManagerVO currentManager = manager.getManager();
		System.out.println("기존 매니저 정보 : " + currentManager);
		System.out.println("입력받은 매니저 객체 : " + rm);
		if(!currentManager.getRm_id().equals(rm.getRm_id())){
			System.out.println("매니저 정보 변경 실패!");
			return "redirect:/manager/managerpage";
		}
		System.out.println("매니저 정보 일치!");

		managerService.updateManagerInfo(rm);

		System.out.println("수정 중.....");
		currentManager.setRm_email(rm.getRm_email());
		currentManager.setRm_name(rm.getRm_name());
		currentManager.setRm_phone(rm.getRm_phone());
		currentManager.setRm_pw(rm.getRm_pw());

		return "manager/main";
	}
	
	//매장 편의시설 목록 페이지
	@GetMapping("/resfacility/resfacilitylist/{rt_num}")
	public String facilityListPage(Model model, @PathVariable int rt_num, @AuthenticationPrincipal CustomManager manager) {
		
		
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/login";
		}

		if(rt_num != manager.getManager().getRm_rt_num()){
			 return "redirect:/manager";
		}
		
		
		
		System.out.println("resfacilitylist rt_num: " + rt_num);
		List<FacilityVO> facility = managerService.getFacilityList();
		List<RestaurantFacilityVO> facilitylist = managerService.getResFacilityList(rt_num);
		System.out.println("등록된 편의 시설 : "+facility);
		
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("facility", facility);
		model.addAttribute("facilitylist", facilitylist);
		model.addAttribute("manager", manager.getManager());
		return "manager/resfacility/resfacilitylist";
	}

	//매장 편의시설 등록 페이지
	@GetMapping("/resfacility/make_resfacility")
	public String makeResFacilityPage(Model model, @AuthenticationPrincipal CustomManager manager) {
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }
		List<FacilityVO> facility = managerService.getFacilityList();
		model.addAttribute("facility", facility);
		model.addAttribute("url", "/make_resfacility");
		return "/manager/resfacility/make_resfacility";
	}

	@PostMapping("/resfacility/make_resfacility")
	public String insertResFacility(Model model, RestaurantFacilityVO resfacility,  @AuthenticationPrincipal CustomManager manager) {
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }

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
		model.addAttribute("errorMsg", "등록에 실패했습니다. 다시 시도해 주세요.");
        model.addAttribute("showModal", true);
		return "redirect:/manager/resfacility/resfacilitylist/"+rtNum;
	}

	//편의시설 정보 변경
	@GetMapping("/resfacility/remake_resfacility/{rf_num}")
	public String reMakeResFacilityPage(Model model, @AuthenticationPrincipal CustomManager manager, @PathVariable int rf_num) {
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }
		RestaurantFacilityVO resfacility = managerService.getResFacility(rf_num);
		List<FacilityVO> facility = managerService.getFacilityList();
		System.out.println(resfacility);

		model.addAttribute("facility", facility);
		model.addAttribute("resfacility", resfacility);
		model.addAttribute("url", "/remake_resfacility");
		return "/manager/resfacility/remake_resfacility";
	}
	
	@PostMapping("/resfacility/remake_resfacility")
	@ResponseBody
	public ResponseEntity<String> updateResFacility(@RequestBody RestaurantFacilityVO resfacility, 
													@AuthenticationPrincipal CustomManager manager) {
		int rtNum = manager.getManager().getRm_rt_num();
		resfacility.setRf_rt_num(rtNum);
		//매니저 정보가 없는데 실행한 경우
		if (manager == null || manager.getManager() == null) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
    }

		System.out.println("매니저: " + manager.getManager());
		System.out.println("수정 요청된 시설: " + resfacility);

		

		if (rtNum <= 0) {
			return ResponseEntity.badRequest().body("매장 정보 없음");
		}

		boolean result = managerService.remakeResFacility(resfacility);

		if (result) {
			return ResponseEntity.ok("success");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
		}
	}

	@PostMapping("/resfacility/delete_resfacility/{rf_num}")
	public String deleteResFacility(@AuthenticationPrincipal CustomManager manager, @PathVariable int rf_num) {
		//매니저 정보가 없는 경우
		if (manager == null || manager.getManager() == null) {
        return "redirect:/manager/login";
    }
		int rtNum = manager.getManager().getRm_rt_num();
		 if(managerService.deleteResFacility(rf_num)) {
        return "redirect:/manager/resfacility/resfacilitylist/"+rtNum;
    }
		return "redirect:/manager/resfacility/resfacilitylist/"+rtNum;
	}

	//결제내역 페이지
	@GetMapping("/manager_pay/pay")
	public String paymentPage(Model model, @AuthenticationPrincipal CustomManager manager){
		if(manager.getManager().getRm_rt_num()==0 || 
			 manager == null || manager.getManager() == null){
			return "redirect:/manager/login";
		}

		List<PaymentVO> paymentList = paymentService.getPaymentList(manager.getManager().getRm_rt_num());
		RestaurantVO restaurant = managerService.getRestaurantByNum(manager.getManager().getRm_rt_num());
		List<BusinessDateVO> operTimeList =managerService.getOperTimeList(manager.getManager().getRm_rt_num());
		System.out.println("-------------------");
		System.out.println("예약 결제 내역"+paymentList);
		System.out.println("-------------------");
		System.out.println("영업 일자"+operTimeList);
		System.out.println("-------------------");

		model.addAttribute("operTimeList", operTimeList);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("manager",manager.getManager());
		model.addAttribute("paymentList", paymentList);
		return "/manager/manager_pay/pay";
	}
	
	//매니저 예약 현황
	@GetMapping("/reservation")
	public String reservateion(Model model, @AuthenticationPrincipal CustomManager manager) {
		if( manager.getManager().getRm_rt_num()==0 || 
			 manager == null || manager.getManager() == null){
			return "redirect:/manager/login";
		}
		int rt_num = manager.getManager().getRm_rt_num();
		List<BusinessDateVO> operTimeList = managerService.getOperTimeList(rt_num);
		List<BusinessHourVO> resTimeList = managerService.getResTimeList(rt_num);
		List<ReservationVO> reservationList = reservationService.getReservations(rt_num);

		System.out.println("예약 가능 시간대 : " + resTimeList);

		
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("operTimeList", operTimeList);
		model.addAttribute("resTimeList", resTimeList);
		model.addAttribute("reservationList", reservationList);
		return "/manager/reservation/reservation";
	}

	@PostMapping("/reservation/date")
	@ResponseBody
	public List<ReservationVO> reservationDate(Model model, @AuthenticationPrincipal CustomManager customManager, @RequestParam String date) {
		return reservationService.getReservationList(customManager, date);
	}

	@PostMapping("/reservation/state")
	@ResponseBody
	public Map<String, Object> reservateionState(@RequestBody ReservationVO reservation, HashMap<String, Object> map) {
		System.out.println("예약 : " + reservation);
		System.out.println("맵 : " + map);
		try{
			map.put("res", reservationService.updateReservationState(reservation));
		}catch(Exception e){
			map.put("error", e.getMessage());
		}
		System.out.println("맵 : " + map);
		return map;
	}

	//매니저 예약 허용된 예약 목록
	@GetMapping("/reservation/reservationlist")
	public String reservationList(Model model,  @AuthenticationPrincipal CustomManager manager) {
		if( manager.getManager().getRm_rt_num()==0 || 
			 manager == null || manager.getManager() == null){
			return "redirect:/manager/login";
		}
		int rt_num = manager.getManager().getRm_rt_num();
		List<BusinessDateVO> operTimeList = managerService.getOperTimeList(rt_num);
		List<BusinessHourVO> resTimeList = managerService.getResTimeList(rt_num);
		List<ReservationVO> reservationList = reservationService.getReservations(rt_num);
		System.out.println("manager : "+manager.getManager());
		
		System.out.println("---------------------");
		System.out.println("operTimeList"+operTimeList);
		System.out.println("---------------------");
		System.out.println("resTimeList"+resTimeList);
		System.out.println("---------------------");
		System.out.println("reservationList"+reservationList);
		System.out.println("---------------------");

		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("manager", manager);
		model.addAttribute("operTimeList", operTimeList);
		model.addAttribute("resTimeList", resTimeList);
		model.addAttribute("reservationList", reservationList);
		//return "/manager/reservation/reservationlist";
		return "/manager/reservation/reservationlist";
	}

	//매니저 예약 시간 템플릿
	@GetMapping("/restime/restimetemplate/{rt_num}")
	public String restimeTemplate(Model model, @PathVariable int rt_num, @AuthenticationPrincipal CustomManager manager) {
		if(manager == null || manager.getManager() == null || manager.getManager().getRm_rt_num() <= 0) {
			return "redirect:/manager/login";
		}
		RestaurantManagerVO getOner = managerService.getManagerOner(rt_num);
		System.out.println("매장 기본키: "+rt_num);
		System.out.println("해당 페이지의 매니저 : "+ getOner.getRm_id());
		System.out.println("접속한 아이디 : " + manager.getManager().getRm_id());

		if(!manager.getManager().getRm_id().equals(getOner.getRm_id())){
				return "redirect:/manager";
		}


		List<BusinessDateVO> opertimeList = managerService.getOperTimeList(rt_num);
		List<BusinessHourTemplateVO> templateList =managerService.getTemplateList(rt_num);
		System.out.println("opertimeList : "+opertimeList);
		if (!opertimeList.isEmpty()) {
				System.out.println("첫 번째 open: " + opertimeList.get(0).getBd_open());
		}

		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("opertimeList", opertimeList);
		model.addAttribute("templateList", templateList != null ? templateList : new ArrayList<>());
		model.addAttribute("manager", manager.getManager());
		return "/manager/restime/restimetemplate";
	}

	//예약 시간 템플릿 저장
	@PostMapping("/restime/restimetemplate/save")
	@ResponseBody
	public ResponseEntity<?> saveTemplate(@RequestBody List<BusinessHourTemplateVO> templateList) {
			try {
					System.out.println("받은 데이터: " + templateList);
					managerService.saveTemplates(templateList);
					return ResponseEntity.ok().build();
			} catch (Exception e) {
					e.printStackTrace();  // 로그 출력
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장 실패: " + e.getMessage());
			}
	}
	

}
	




