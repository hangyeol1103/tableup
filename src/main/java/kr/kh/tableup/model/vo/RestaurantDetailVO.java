package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class RestaurantDetailVO {
	int rd_num;
	int rd_rt_num;
	String rd_phone;
	String rd_closed_days;
	String rd_info;
	String rd_home;
	String rd_addr;
}
