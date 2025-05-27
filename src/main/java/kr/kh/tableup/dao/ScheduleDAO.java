package kr.kh.tableup.dao;

import java.util.List;

import kr.kh.tableup.model.vo.BusinessDateVO;
import kr.kh.tableup.model.vo.BusinessHourVO;

public interface ScheduleDAO {

	List<BusinessDateVO> selectScheduleOperTimeList(int rt_num);

	BusinessDateVO selectOperTimeByDay(String date);
	
}
