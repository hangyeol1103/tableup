package kr.kh.tableup.model.util;

import lombok.Data;

@Data
public class RevCriteria extends Criteria {
	
	private int rev_num;
	
	private int rt_num;

	public String toString(){
		return super.toString() +", " + rev_num + ", " + rt_num; 
	}
}
