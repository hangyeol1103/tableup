package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class FoodCategoryVO {
	int fc_num;	//카테고리 번호
	String fc_main;	//카테고리 대분류

	
	int dfc_num;	//디테일 번호
	String dfc_sub;	//카테고리 소분류
	//int dfc_fc_num; 	//카테고리 번호
}
