package kr.kh.tableup.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import kr.kh.tableup.model.util.CustomManager;
import kr.kh.tableup.model.vo.BusinessDateVO;
import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.BusinessHourVO22;
import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.service.ManagerService;
import kr.kh.tableup.service.ReservationService;
import kr.kh.tableup.service.ScheduleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

		int rt_num = manager.getManager().getRm_rt_num();
		System.out.println("나의 정보 : " + manager.getManager());
		System.out.println("접속한 아이디 : " + principal);
		if(rt_num <=0){
			model.addAttribute("restaurant", null);
		}
		else{
			RestaurantVO restaurant = managerService.getRestaurantByNum(rt_num);
			List<BusinessDateVO> opertimelist = scheduleService.getOperTimeList(rt_num);
			
			List<BusinessHourVO> restimelist = managerService.getResTimeList(rt_num);
			//터미널에 필요한 매장 정보 및 세부 정보들 출력

      model.addAttribute("restaurant", restaurant);
      model.addAttribute("opertimelist", opertimelist);
      model.addAttribute("restimelist", restimelist);

			System.out.println("나의 매장 : " + restaurant);
			System.out.println("나의 매장 영업 일자 : " + opertimelist);
		}
		
		// List<LocalDate> dateList= new ArrayList<>();
		// LocalDate today = LocalDate.now();
		// for(int i=0;i<7;i++){
		// 	dateList.add(today.plusDays(i));
		// }

		// model.addAttribute("dateList", dateList);
		model.addAttribute("offset", offset);
		model.addAttribute("manager", manager);
		model.addAttribute("url","/manager_schedulelist");
		return "schedule/manager_schedulelist";
	}

	@GetMapping("/business-date")
	@ResponseBody
	public BusinessDateVO getBusinessDate(@RequestParam String date) {
		BusinessDateVO db = scheduleService.getOperTimeByDay(date);
		return db; // JSON 반환
	}

	// 날짜 선택시 날짜와 같은 예약 가능 시간 버튼으로 출력
	@GetMapping("/getTimes")
	@ResponseBody
	public List<String> getFilteredTimes(@RequestParam("selectedDate")String selectedDate, @AuthenticationPrincipal CustomManager manager) {
		int rt_num=manager.getManager().getRm_rt_num();

		if (selectedDate == null || selectedDate.isBlank()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "선택된 날짜가 비어 있습니다.");
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = LocalDate.parse(selectedDate, formatter);
    LocalDate endDate = startDate.plusDays(6);

		System.out.println("selectedDate : "+ selectedDate);
		System.out.println("formatter : "+ formatter);
		System.out.println("startDate : "+ startDate);
		System.out.println("endDate : "+ endDate);

		List<BusinessHourVO> selectedResStart = scheduleService.getResStart(rt_num, startDate, endDate);
		System.out.println("리스트 : " + selectedResStart);
		
		//  return selectedResStart.stream()
    //    .filter(bh -> bh.getBh_start().toLocalDate().equals(startDate)) // 선택 날짜만 필터
    //    .map(bh -> bh.getBh_start().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
    //    .collect(Collectors.toList());
		
		return selectedResStart.stream()
			.filter(bh -> {
					try {
							// bh_start: "2025-05-29 15:00:00" 같은 포맷일 경우
							return bh.getBh_start() != null &&
										bh.getBh_start().startsWith(selectedDate); // 날짜 부분 비교
					} catch (Exception e) {
							return false;
					}
			})
			.map(bh -> bh.getBh_start().substring(11, 16)) // 시간 부분만 추출 (HH:mm)
			.collect(Collectors.toList());

	}

	//예약 가능 시간 인원수 정보 출력
	@GetMapping("/time-infomation")
	@ResponseBody
	public Map<String, Object> getTimeInfomation(@RequestParam String date, @RequestParam String time, @AuthenticationPrincipal CustomManager manager) {
		int rt_num=manager.getManager().getRm_rt_num();
		LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));

		BusinessHourVO restimedetail=scheduleService.getResTimeDetail(rt_num,dateTime);
		Map<String, Object> result= new HashMap<>();

		
		if (restimedetail != null) {
        result.put("seatCount", restimedetail.getBh_seat_current());
        result.put("seatMax", restimedetail.getBh_seat_max());
    }

		return result;
		}

		//쉬는날 변경 기능
		@PostMapping("/update-holiday")
		@ResponseBody
		public ResponseEntity<Boolean> updateHoliday(@RequestBody BusinessDateVO date) {
			
			System.out.println("bd_date: " + date.getBd_date());
    	System.out.println("bd_off: " + date.isBd_off());
			
			boolean result = scheduleService.updateBdOff(date);
			return ResponseEntity.ok(result);
		}

		//일정관리 페이지에서 예약 시간 추가

		//@RequestBody Map<String, Object> data , @RequestBody BusinessHourVO data
		@PostMapping("/insertRestime")
		public ResponseEntity<?> insertRestime(@RequestBody Map<String, Object> data , @AuthenticationPrincipal CustomManager manager) {
			try{
				System.out.println("data : " + data);
				 String date=(String)data.get("date");
				 String startTime=(String)data.get("startTime");
				 String endTime=(String)data.get("endTime");
				 int seatMax = (int)data.get("seatMax");
				 int tableMax = (int)data.get("tableMax");

				LocalDateTime start = LocalDateTime.parse(date + "T" + startTime);
        LocalDateTime end = LocalDateTime.parse(date + "T" + endTime);

				BusinessHourVO res = new BusinessHourVO();
				// res.setBh_start(start);
				// res.setBh_end(end);
				res.setBh_start(start.toString());
				res.setBh_end(end.toString());
				res.setBh_seat_max(seatMax);
				res.setBh_table_max(tableMax);
				res.setBh_state(false);
				res.setBh_rt_num(manager.getManager().getRm_rt_num());

				boolean result = scheduleService.insertRestime(res);

				return result ? ResponseEntity.ok().build()
							: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

			}catch(Exception e){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못 입력했습니다.");
			}
			
		}
		
	

}
