package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class ResNewsVO {
	int rn_num;
	String rn_content;
	boolean rn_state;
	int rn_rt_num;
}
