package kr.kh.tableup.dao;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Param;

import kr.kh.tableup.model.vo.BusinessHourVO22;

public interface BusinessHourDAO {

	boolean updateCurrentSeat(@Param("bh_num") int bh_num, @Param("seat") int updated);

	BusinessHourVO22 selectBusinessHourByBh_start(LocalDateTime time);

}
