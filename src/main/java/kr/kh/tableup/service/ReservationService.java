package kr.kh.tableup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.ReservationDAO;
import kr.kh.tableup.model.vo.ReservationVO;

@Service
public class ReservationService {

	@Autowired
	ReservationDAO reservationDAO;

	/** 예약 등록 */
	public boolean insertReservation(ReservationVO res) {
		if (res == null || res.getRes_person() < 1 || res.getRes_rt_num() < 1 || res.getRes_us_num() < 1) {
			return false;
		}
		return reservationDAO.insertReservation(res);
	}

}
