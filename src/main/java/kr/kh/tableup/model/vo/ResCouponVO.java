package kr.kh.tableup.model.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ResCouponVO {
	int rec_num;
	String rec_content;
	boolean rec_state;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date rec_period;
	
	int rec_rt_num;
}
