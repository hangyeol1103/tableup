package kr.kh.tableup.model.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BusinessHourTemplateVO {
	int bhd_num;
	String bhd_date;
	@DateTimeFormat(pattern = "HH:mm")
	private Date bhd_timeStart;
	@DateTimeFormat(pattern = "HH:mm")
	private Date bhd_timeEnd;
	int bhd_seat;
	int bhd_table;
	int bhd_rt_num;
}
