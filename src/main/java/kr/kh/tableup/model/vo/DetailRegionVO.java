package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class DetailRegionVO {
	int dreg_num;
	String dreg_sub;
	int dreg_reg_num;

	int reg_num;	//지역 대분류 번호
	String reg_main;	//지역 대분류 이름


	
}


