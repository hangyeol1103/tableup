package kr.kh.tableup.model.vo;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import lombok.Data;

@Data
public class BusinessHourVO {
	int bh_num;
	
	@DateTimeFormat(pattern = "HH:mm")
	LocalDateTime bh_start;
	@DateTimeFormat(pattern = "HH:mm")
	LocalDateTime bh_end;
	int bh_seat_max;
	int bh_seat_current;
	int bh_table_max;
	int bh_table_current;
	boolean bh_state;
	int bh_rt_num;
}
