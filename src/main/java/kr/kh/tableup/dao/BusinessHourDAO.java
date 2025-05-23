package kr.kh.tableup.dao;

import org.apache.ibatis.annotations.Param;

public interface BusinessHourDAO {

	void updateCurrentSeat(@Param("bh_num") int bh_num, @Param("seat") int updated);

}
