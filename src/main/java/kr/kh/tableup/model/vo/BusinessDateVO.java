package kr.kh.tableup.model.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BusinessDateVO {
	private int bd_num;
	
	//@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String bd_date;
	
	private int bd_rt_num;
	private boolean bd_off;
	private String bd_open;
	private String bd_close;
	private String bd_brstart;
	private String bd_brend;
	private String bd_loam;
	private String bd_lopm;
	
}
