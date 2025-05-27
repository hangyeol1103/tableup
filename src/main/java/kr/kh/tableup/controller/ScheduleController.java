package kr.kh.tableup.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.kh.tableup.model.util.CustomManager;
import kr.kh.tableup.model.vo.BusinessDateVO;
import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.service.ManagerService;
import kr.kh.tableup.service.ReservationService;
import kr.kh.tableup.service.ScheduleService;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {
	
	@Autowired
	ManagerService managerService;

	@Autowired
	ReservationService reservationService;

	@Autowired
	ScheduleService scheduleService;

	@GetMapping("/manager_schedulelist")
	public String manager(@RequestParam(defaultValue = "0") int offset, Model model, @AuthenticationPrincipal CustomManager manager, Principal principal ) {
		String loginId =principal.getName();
		if (manager == null || loginId !=manager.getManager().getRm_id()) {
        return "redirect:/manager/login";
    }
		System.out.println(manager);

		int rt_num = manager.getManager().getRm_rt_num();

		if(rt_num <=0){
			model.addAttribute("restaurant", null);
		}
		else{
			RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
			List<BusinessDateVO> opertimelist = scheduleService.getOperTimeList(rt_num);
			
			List<BusinessHourVO> restimelist = managerService.getResTimeList(rt_num);
			//터미널에 필요한 매장 정보 및 세부 정보들 출력
			System.out.println(restaurant);
			System.out.println(opertimelist);
			System.out.println(restimelist);

      model.addAttribute("restaurant", restaurant);
      model.addAttribute("opertimelist", opertimelist);
      model.addAttribute("restimelist", restimelist);
		}
		
		List<ReservationVO> reservationList = reservationService.getScheduleReservationList(rt_num);
		List<LocalDate> dateList= new ArrayList<>();
		LocalDate today = LocalDate.now();
		for(int i=0;i<7;i++){
			dateList.add(today.plusDays(i));
		}
		List<LocalTime> timeList = new ArrayList<>();
		LocalTime startTime = LocalTime.of(0, 0);
    for (int i = 0; i < 48; i++) {
        timeList.add(startTime.plusMinutes(30 * i));
    }

		model.addAttribute("reservationList", reservationList);
		model.addAttribute("dateList", dateList);
		model.addAttribute("timeList", timeList);
		model.addAttribute("offset", offset);
		model.addAttribute("manager", manager);
		model.addAttribute("url","/manager_schedulelist");
		return "schedule/manager_schedulelist";
	}

	@GetMapping("/business-date")
	@ResponseBody
	public BusinessDateVO getBusinessDate(@RequestParam String date) {
		System.out.println(date);
		BusinessDateVO db = scheduleService.getOperTimeByDay(date);
		System.out.println(db);
		return db; // JSON 반환
	}

}
