package kr.kh.tableup.model.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BusinessDateVO {
	private int bd_num;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date bd_date;
	
	private int bd_rt_num;
	private boolean bd_off;
	private Date bd_open;
	private Date bd_close;
	private Date bd_brstart;
	private Date bd_brend;
	private Date bd_loam;
	private Date bd_lopm;
	
}
