package kr.kh.tableup.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.ReservationVO;

public interface ReservationDAO {

	// 1. 예약 등록
	boolean insertReservation(ReservationVO reservation);
	boolean updateReservationState(@Param("res_num") int res_num, @Param("state") int state);

	List<BusinessHourVO> selectOverlapHours(@Param("rt_num") int rt_num,
	                                        @Param("resStart") LocalDateTime resStart,
  	                                      @Param("resEnd") LocalDateTime resEnd);

	List<ReservationVO> selectExpiredReservations(LocalDateTime now);
	
	//List<ReservationVO> selectReservationList(int rt_num);

	List<ReservationVO> selectReservationList(int rt_num, String date);
	
	ReservationVO selectReservation(int res_num);


}
