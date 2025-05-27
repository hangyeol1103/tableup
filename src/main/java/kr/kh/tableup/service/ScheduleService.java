package kr.kh.tableup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.ScheduleDAO;
import kr.kh.tableup.model.vo.BusinessDateVO;
import kr.kh.tableup.model.vo.BusinessHourVO;


@Service
public class ScheduleService {

	@Autowired
	ScheduleDAO scheduleDAO;
	
	public List<BusinessDateVO> getOperTimeList(int rt_num) {
		return scheduleDAO.selectScheduleOperTimeList(rt_num);
	}

	public BusinessDateVO getOperTimeByDay(String date) {
		System.out.println("--------------");
		System.out.println(date);
		return scheduleDAO.selectOperTimeByDay(date);
	}
	
}
