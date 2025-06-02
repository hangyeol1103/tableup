package kr.kh.tableup.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import kr.kh.tableup.model.vo.BusinessDateVO;
import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.BusinessHourVO22;

public interface ScheduleDAO {

	List<BusinessDateVO> selectScheduleOperTimeList(int rt_num);

	BusinessDateVO selectOperTimeByDay(String date);

	List<BusinessHourVO> selectResTime(int rt_num, LocalDate startDate, LocalDate endDate);

	BusinessHourVO selectResTimeDetail(int rt_num, LocalDateTime dateTime);

	boolean updateBdOff(BusinessDateVO date);

	boolean insertResTime(BusinessHourVO res);

	BusinessHourVO checkResTime(int bh_rt_num, String bh_start, String bh_end);
	
}
