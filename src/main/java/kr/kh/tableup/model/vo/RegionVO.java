package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class RegionVO {
	int reg_num;	//지역 대분류 번호
	String reg_main;	//지역 대분류 이름

	int dreg_num;	//지역 소분류 번호
	String dreg_sub;	//지역 소분류 이름
	//int dreg_reg_num;	//지역 대분류 번호
}
