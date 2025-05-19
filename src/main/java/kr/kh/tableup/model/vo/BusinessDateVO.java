package kr.kh.tableup.model.vo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BusinessDateVO {
	private int bd_num;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String bd_date;
	
	private int bd_rt_num;

	private boolean bd_off;
	@DateTimeFormat(pattern = "HH:mm")
	private Date bd_open;
	@DateTimeFormat(pattern = "HH:mm")
	private Date bd_close;
	@DateTimeFormat(pattern = "HH:mm")
	private Date bd_brstart;
	@DateTimeFormat(pattern = "HH:mm")
	private Date bd_brend;
	@DateTimeFormat(pattern = "HH:mm")
	private Date bd_loam;
	@DateTimeFormat(pattern = "HH:mm")
	private Date bd_lopm;
	

	private LocalDate bd_local_date;

	
}
