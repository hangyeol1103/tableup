package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class DetailFoodCategoryVO {
	int dfc_num;
	String dfc_sub;
	int dfc_fc_num;


  public int getDfc_num() {
     return dfc_num;
  }
  public String getDfc_sub() {
      return dfc_sub;
  }
  public int getDfc_fc_num() {
      return dfc_fc_num;
  }
}
