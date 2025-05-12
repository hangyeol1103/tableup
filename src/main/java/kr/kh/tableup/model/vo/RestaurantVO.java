package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class RestaurantVO {
	
	int rt_num;
	String rt_name;
	int rt_reg_num;
	int rt_fc_num;
	String rt_closed_days;
	String rt_price_lunch;
	String rt_price_dinner;
	String rt_image;
	String rt_accept;
}
