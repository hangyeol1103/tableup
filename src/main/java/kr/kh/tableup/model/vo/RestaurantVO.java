package kr.kh.tableup.model.vo;

import java.util.List;

import lombok.Data;

@Data
public class RestaurantVO {
	
	int rt_num;
	String rt_name;
	int rt_dreg_num;
	int rt_dfc_num;
	String rt_closed_days;
	String rt_price_lunch;
	String rt_price_dinner;
	String rt_accept;
	String rt_description;

	int rt_max_table;
	int rt_max_person;

	//디테일
	int rd_num;
	int rd_rt_num;
	String rd_phone;
	String rd_closed_days;
	String rd_info;
	String rd_home;
	String rd_addr;

	int reg_num;
	int fc_num;

	List<TagVO> tagList;
	List<ReviewVO> reviewList;

}
