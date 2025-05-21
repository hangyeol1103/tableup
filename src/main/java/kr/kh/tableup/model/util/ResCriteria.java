package kr.kh.tableup.model.util;

import lombok.Data;

@Data
public class ResCriteria extends Criteria {
	
	private int rt_dfc_num;
	private int rt_dreg_num;
	String orderby;
}
