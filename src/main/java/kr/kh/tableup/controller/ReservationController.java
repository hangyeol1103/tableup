package kr.kh.tableup.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.kh.tableup.dao.ReservationDAO;
import kr.kh.tableup.dao.RestaurantDAO;
import kr.kh.tableup.dao.UserDAO;
import kr.kh.tableup.model.util.CustomManager;
import kr.kh.tableup.model.vo.BusinessHourVO22;
import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.model.vo.UserVO;
import kr.kh.tableup.service.ManagerService;
import kr.kh.tableup.service.ReservationService;
import kr.kh.tableup.service.UserService;

@Controller
@RequestMapping("/user/reservation")
public class ReservationController {

	@Autowired
	ManagerService managerService;

	@Autowired
	UserService userService;

	@Autowired
	ReservationService reservationService;



	@GetMapping("/confirm")
	public String reservationConfirm(@RequestParam int rt_num, @RequestParam String date, @RequestParam String time,
																	 @RequestParam int person, Model model) {
		RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("rt_num", rt_num);
		model.addAttribute("date", date);
		model.addAttribute("time", time);
		model.addAttribute("person", person);
		return "user/reservation/confirm";
	}

	@PostMapping("/submit")
	public String reservationSubmit(@RequestParam int rt_num, @RequestParam String date, @RequestParam String time,
																	@RequestParam int person, @RequestParam(required = false) String request, Principal principal, RedirectAttributes ra) {
		if (principal == null) {
			ra.addFlashAttribute("msg", "로그인이 필요합니다.");
			return "redirect:/user/login";
		}
		String loginId = principal.getName();
		UserVO user = userService.selectUserById(loginId);
		LocalDateTime resTime;
		try {
			resTime = LocalDateTime.parse(date + " " + time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		} catch (Exception e) {
			ra.addFlashAttribute("msg", "잘못된 날짜 형식입니다.");
			return "redirect:/";
		}

		LocalDateTime resEndTime = resTime.plusHours(2);

		boolean available = reservationService.isReservationAvailable(rt_num, resTime, resEndTime, person);
		if (!available) {
			ra.addFlashAttribute("msg", "해당 시간대 좌석이 부족합니다.");
			return "redirect:/";
		}

		ReservationVO res = new ReservationVO();
		res.setRes_us_num(user.getUs_num());
		res.setRes_rt_num(rt_num);
		res.setRes_time(resTime);
		res.setRes_end_time(resEndTime);
		res.setRes_person(person);
		res.setRes_request(request);
		res.setRes_created(LocalDateTime.now());
		res.setRes_state(0);

		boolean result = reservationService.insertReservation(res);
		if (!result) {
			ra.addFlashAttribute("msg", "예약 실패. 다시 시도해주세요.");
			return "redirect:/";
		}

		ra.addFlashAttribute("msg", "예약이 완료되었습니다.");
		return "redirect:/";
	}
	
}
