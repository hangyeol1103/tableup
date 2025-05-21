package kr.kh.tableup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.kh.tableup.model.vo.ReservationVO;

public interface ReservationDAO {

	// 1. 예약 등록
	boolean insertReservation(ReservationVO reservation);


}
