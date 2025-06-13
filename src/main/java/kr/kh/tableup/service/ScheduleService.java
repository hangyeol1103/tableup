package kr.kh.tableup.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.ManagerDAO;
import kr.kh.tableup.dao.ScheduleDAO;
import kr.kh.tableup.model.vo.BusinessDateVO;
import kr.kh.tableup.model.vo.BusinessHourVO;


@Service
public class ScheduleService {

	@Autowired
	ScheduleDAO scheduleDAO;

	@Autowired
	ManagerDAO managerDAO;
	
	public List<BusinessDateVO> getOperTimeList(int rt_num) {
		return scheduleDAO.selectScheduleOperTimeList(rt_num);
	}

	public BusinessDateVO getOperTimeByDay(String date) {
		System.out.println("--------------");
		System.out.println(date);
		return scheduleDAO.selectOperTimeByDay(date);
	}

	public List<BusinessHourVO> getResStart(int rt_num, LocalDate startDate, LocalDate endDate) {
		
		return scheduleDAO.selectResTime(rt_num, startDate, endDate);
	}

	public BusinessHourVO getResTimeDetail(int rt_num, LocalDateTime dateTime) {
		return scheduleDAO.selectResTimeDetail(rt_num, dateTime);
	}

	public boolean updateBdOff(BusinessDateVO date) {
		return scheduleDAO.updateBdOff(date);
	}

	public boolean insertRestime(BusinessHourVO res, String date) {
			if (res == null || res.getBh_start() == null || res.getBh_end() == null) {
					return false;
			}

			System.out.println("date : " + date);
			System.out.println("res : " + res);
			int rt_num = res.getBh_rt_num();

			BusinessDateVO day = managerDAO.selectOperTimeByDate(rt_num, date);
			if (day == null) {
					System.out.println("해당 날짜의 영업일자 정보 없음");
					return false;
			}
			System.out.println("영업일자 정보: " + day);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			LocalDateTime bhStart = res.getBh_start();
			LocalDateTime bhEnd = res.getBh_end();

			LocalTime resStart = bhStart.toLocalTime();
			LocalTime resEnd = bhEnd.toLocalTime();

			LocalTime open = LocalDateTime.parse(day.getBd_open(), formatter).toLocalTime();
			LocalTime close = LocalDateTime.parse(day.getBd_close(), formatter).toLocalTime();

			System.out.println("resStart : " + resStart);
			System.out.println("resEnd : " + resEnd);
			System.out.println("open : " + open);
			System.out.println("close : " + close);

			System.out.println("----------------------");

			// 브레이크 타임 체크
			String brStartStr = day.getBd_brstart();
			String brEndStr = day.getBd_brend();

			if (brStartStr != null && !brStartStr.isBlank() &&
					brEndStr != null && !brEndStr.isBlank()) {
					try {
							LocalTime brStart = LocalDateTime.parse(brStartStr, formatter).toLocalTime();
							LocalTime brEnd = LocalDateTime.parse(brEndStr, formatter).toLocalTime();

							if (resStart.isBefore(brEnd) && resEnd.isAfter(brStart)) {
									System.out.println("브레이크 타임 겹침");
									return false;
							}
					} catch (DateTimeParseException e) {
							System.out.println("브레이크 타임 파싱 오류: " + e.getMessage());
					}
			}

			System.out.println("----------------------");

			// 영업시간 범위 체크
			if (resStart.isBefore(open) || resEnd.isAfter(close)) {
					System.out.println("영업시간 범위 초과");
					return false;
			}

			// 중복 체크
			BusinessHourVO check = scheduleDAO.checkResTime(
					res.getBh_rt_num(),
					res.getBh_start(),
					res.getBh_end()
			);

			System.out.println("check : " + check);
			if (check != null) {
					System.out.println("중복 예약 시간");
					return false;
			}

			return scheduleDAO.insertResTime(res);
	}
	
}
