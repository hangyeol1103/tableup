package kr.kh.tableup.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetailFoodCategoryVO {
	int dfc_num;    //디테일 카테고리 번호
	String dfc_sub; //소분류 이름
	int dfc_fc_num; //대분류 번호

  String fc_main; //대분류 이름
  int fc_num;  //대분류 번호


}
