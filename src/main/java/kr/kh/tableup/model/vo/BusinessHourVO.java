package kr.kh.tableup.model.vo;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BusinessHourVO {
	private int bh_num;
	
	@DateTimeFormat(pattern = "HH:mm")
	private LocalDateTime bh_start;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalDateTime bh_end;
	private int bh_seat_max;
	private int bh_seat_current;
	private int bh_table_max;
	private int bh_table_current;
	private boolean bh_state;
	private int bh_rt_num;

	private int bh_seat_remain;
	private int bh_table_remain;
}
