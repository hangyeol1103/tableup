package kr.kh.tableup.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
