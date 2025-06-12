package kr.kh.tableup.model.vo;

import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class DefaultResTimeVO {
	int drt_num;
	int drt_rt_num;
	//"ENUM"
	String drt_date;
	boolean drt_off;
	@DateTimeFormat(pattern = "HH:mm")
	LocalTime drt_open;
	@DateTimeFormat(pattern = "HH:mm")
	LocalTime drt_close;
	@DateTimeFormat(pattern = "HH:mm")
	LocalTime drt_brstart;
	@DateTimeFormat(pattern = "HH:mm")
	LocalTime drt_brend;
	@DateTimeFormat(pattern = "HH:mm")
	LocalTime drt_loam;
	@DateTimeFormat(pattern = "HH:mm")
	LocalTime drt_lopm;
}
