package kr.kh.tableup.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.management.RuntimeErrorException;

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
			List<BusinessHourVO > bhList = reservationDAO.selectOverlapHours(
				res.getRes_rt_num(), res.getRes_time(), res.getRes_end_time());

		// 좌석 누적 갱신
		for (BusinessHourVO  bh : bhList) {
			int updated = bh.getBh_seat_current() + res.getRes_person();
			businessHourDAO.updateCurrentSeat(bh.getBh_num(), updated);
		}

		return true;
	}

	public boolean isReservationAvailable(int rt_num, LocalDateTime resStart, LocalDateTime resEnd, int person) {
		List<BusinessHourVO > bhList = reservationDAO.selectOverlapHours(rt_num, resStart, resEnd);

		for (BusinessHourVO  bh : bhList) {
			int remaining = bh.getBh_seat_max() - bh.getBh_seat_current();
			if (remaining < person) {
				return false;
			}
		}

		return true;
	}
/*
	public List<ReservationVO> getReservation(int rt_num) {
		return reservationDAO.selectReservationList(rt_num);
	*/
		public List<ReservationVO> getReservationList(CustomManager customManager, String date) {
		//매니저 로그인이 아닌 경우
		if(customManager == null){
			return null;
		}
		RestaurantManagerVO rm = customManager.getManager();
		
		return reservationDAO.selectReservationList(rm.getRm_rt_num(), date);
	}

		public boolean updateReservationState(ReservationVO reservation) {
			if(reservation == null || reservation.getRes_state() == 0){
				throw new RuntimeException("올바른 접근이 아닙니다.");
			}
			//예약 확정
			if(reservation.getRes_state() == 1){
				return reservationConfirm(reservation);
			}
			//예약 취소
			else{
				return reservationCancel(reservation);
			}
		}

		private boolean reservationConfirm(ReservationVO reservation) {
			//예약 정보 가져옴
			ReservationVO dbReservation = reservationDAO.selectReservation(reservation.getRes_num());
			if(dbReservation == null){
				throw new RuntimeException("예약 내역이 없습니다.");
			}
			//예약 가능 정보를 가져옴
			BusinessHourVO  businessHour = businessHourDAO.selectBusinessHourByBh_start(dbReservation.getRes_time());
			if(businessHour == null){
				throw new RuntimeException("예약 가능한 시간대가 아닙니다.");
			}
			int count = businessHour.getBh_seat_max() - businessHour.getBh_seat_current();
			//예약이 불가능(예약 인원수가 더 많을 때)하면 false를 리턴
			if(count < dbReservation.getRes_person()){
				throw new RuntimeException("예약 가능 좌석이 부족합니다.\n예약 가능 좌석 : " + count);
			}
			//예약 가능하면 예약 진행
			if(!reservationDAO.updateReservationState(reservation.getRes_num(), reservation.getRes_state())){
				return false;
			}
			//예약 후 현재 예약 좌석을 늘임
			return businessHourDAO.updateCurrentSeat(businessHour.getBh_num(), businessHour.getBh_seat_current() + dbReservation.getRes_person());
		}

		private boolean reservationCancel(ReservationVO reservation) {
			//예약 정보 가져옴
			ReservationVO dbReservation = reservationDAO.selectReservation(reservation.getRes_num());
			if(dbReservation == null){
				throw new RuntimeException("예약 내역이 없습니다.");
			}
			//예약 가능 정보를 가져옴
			BusinessHourVO  businessHour = businessHourDAO.selectBusinessHourByBh_start(dbReservation.getRes_time());
			if(businessHour == null){
				throw new RuntimeException("예약 가능한 시간대가 아닙니다.");
			}
			
			//예약 취소를 진행
			if(!reservationDAO.updateReservationState(reservation.getRes_num(), reservation.getRes_state())){
				return false;
			}
			//예약 대기에서 취소했으면 종료
			if(dbReservation.getRes_state() == 0){
				return true;
			}
			//예약 확정에서 취소 후 현재 예약 좌석을 줄임
			return businessHourDAO.updateCurrentSeat(businessHour.getBh_num(), businessHour.getBh_seat_current() - dbReservation.getRes_person());
		}

}
