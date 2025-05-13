package kr.kh.tableup.model.vo;

import java.util.Date;

import lombok.Data;

@Data
public class BusinessDateVO {
	int bd_num;
	Date bd_date;
	int bd_rt_num;
	boolean bd_off;
	String bd_open;
	String bd_close;
	String bd_brstart;
	String bd_brend;
	String bd_loam;
	String bd_lopm;
	
}
