package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class ReviewScoreVO {
	private int rs_num;
	private int rs_rev_num;
	private int rs_st_num;
	private int rs_score;

	//결합하기 귀찮아서 스코어타입 가져옴
	private int st_num;           // ST_NUM int AI PK
	private String st_category;   // ST_CATEGORY varchar(50)

}
