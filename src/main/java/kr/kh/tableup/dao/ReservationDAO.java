package kr.kh.tableup.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.ReservationVO;

public interface ReservationDAO {

	// 1. 예약 등록
	boolean insertReservation(ReservationVO reservation);

	List<BusinessHourVO> selectOverlapHours(@Param("rt_num") int rt_num,
	                                        @Param("resStart") LocalDateTime resStart,
  	                                      @Param("resEnd") LocalDateTime resEnd);

}
