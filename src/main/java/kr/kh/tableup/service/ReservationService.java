package kr.kh.tableup.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.BusinessHourDAO;
import kr.kh.tableup.dao.ReservationDAO;
import kr.kh.tableup.model.util.CustomManager;
import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.RestaurantManagerVO;

@Service
public class ReservationService {

	@Autowired
	ReservationDAO reservationDAO;

	@Autowired
	BusinessHourDAO businessHourDAO;

	/** 예약 등록 */
	public boolean insertReservation(ReservationVO res) {
		if (res == null || res.getRes_person() < 1 || res.getRes_rt_num() < 1 || res.getRes_us_num() < 1) {
			return false;
		}

		boolean result = reservationDAO.insertReservation(res);
		if (!result)
			return false;
			
			// 예약 성공 후 → 겹치는 시간대 조회
			List<BusinessHourVO> bhList = reservationDAO.selectOverlapHours(
				res.getRes_rt_num(), res.getRes_time(), res.getRes_end_time());

		// 좌석 누적 갱신
		for (BusinessHourVO bh : bhList) {
			int updated = bh.getBh_seat_current() + res.getRes_person();
			businessHourDAO.updateCurrentSeat(bh.getBh_num(), updated);
		}

		return true;
	}

	public boolean isReservationAvailable(int rt_num, LocalDateTime resStart, LocalDateTime resEnd, int person) {
		List<BusinessHourVO> bhList = reservationDAO.selectOverlapHours(rt_num, resStart, resEnd);

		for (BusinessHourVO bh : bhList) {
			int remaining = bh.getBh_seat_max() - bh.getBh_seat_current();
			if (remaining < person) {
				return false;
			}
		}

		return true;
	}

	public List<ReservationVO> getReservationList(CustomManager customManager, String date) {
		//매니저 로그인이 아닌 경우
		if(customManager == null){
			return null;
		}
		RestaurantManagerVO rm = customManager.getManager();
		
		return reservationDAO.selectReservationList(rm.getRm_rt_num(), date);
	}

}
