package kr.kh.tableup.model.util;

import java.util.List;

import lombok.Data;

@Data
public class ResCriteria extends Criteria {

	private String keyword;
	private List<String> keywordList;
	
	private int rt_dfc_num;
	private int rt_dreg_num;

	private String orderBy;

	private int dreg_reg_num;
	private int dfc_fc_num;


	private List<Integer> tagList;
	private List<Integer> facilityList;

	private String priceType;
	private Integer minPrice;
	private Integer maxPrice;
}
