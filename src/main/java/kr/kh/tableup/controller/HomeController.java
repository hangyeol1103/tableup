package kr.kh.tableup.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.RegionVO;
import kr.kh.tableup.model.vo.UserVO;
import kr.kh.tableup.service.ReservationService;
import kr.kh.tableup.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class HomeController {
	
	@Autowired
	UserService userService;

	@Autowired
	ReservationService reservationService;

	@GetMapping("/")
	public String home(Model model) {

		UserVO user = userService.getUserById("123");
		//System.out.println(user);

		model.addAttribute("sample", user.getUs_name());
		model.addAttribute("url", "/");

		// 지역
		//Map<String, List<DetailRegionVO>> regionMap = userService.getRegionMap();
		//model.addAttribute("regionMap", regionMap);


		List<DetailRegionVO> regionList = userService.getRegionListWithWhole();
		List<DetailFoodCategoryVO> foodList = userService.getFoodCategoryListWithWhole();
		int[] favoriteFood = reservationService.favoriteCategory();
		int[] favoriteRegion = reservationService.favoriteRegion();
		model.addAttribute("regionList", regionList);
		model.addAttribute("foodList", foodList);
		model.addAttribute("favoriteFood", favoriteFood);
		model.addAttribute("favoriteRegion", favoriteRegion);

		//System.out.println(regionList);
		System.out.println("foodList: " + foodList);
		System.out.println("favoriteFood: "+ Arrays.toString(favoriteFood));	//sysout favorite하면 안되는구나...신기
		return "index";
	}

	@GetMapping("/test_orive_young")
	public String test1(Model model) {
		UserVO user = userService.getUserById("123");

		model.addAttribute("sample", user.getUs_name());
		model.addAttribute("url", "/test_orive_young");
		List<DetailRegionVO> regionList = userService.getRegionListWithWhole();
		model.addAttribute("regionList", regionList);
		return "/test_orive_young";
	}
	

	
}
