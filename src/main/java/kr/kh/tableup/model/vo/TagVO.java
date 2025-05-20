package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class TagVO {
	int tag_num;
	String tag_name;
	int tag_tt_num;

	int tt_name;	//해당하는 태그타입(대분류) 이름
}
