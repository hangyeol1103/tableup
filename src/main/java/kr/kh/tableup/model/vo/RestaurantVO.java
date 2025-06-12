package kr.kh.tableup.model.vo;

import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
	Integer  rd_num;
	Integer  rd_rt_num;
	String rd_phone;
	String rd_closed_days;
	String rd_info;
	String rd_home;
	String rd_addr;

	int reg_num;
	int fc_num;

	List<TagVO> tagList;
	List<ReviewVO> reviewList;

	DefaultResTimeVO defaultResTime;

	//예비 영업시간 
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

	//썸네일
	String file_path;


}
