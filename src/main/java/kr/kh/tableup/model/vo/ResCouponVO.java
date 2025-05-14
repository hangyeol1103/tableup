package kr.kh.tableup.model.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ResCouponVO {
	int rec_num;
	String rec_content;
	boolean rec_state;
	Date rec_period;
	int rec_rt_num;
}
