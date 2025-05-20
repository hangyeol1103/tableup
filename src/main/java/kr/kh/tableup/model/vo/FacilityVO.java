package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class FacilityVO {
	int fa_num;
	String fa_name;
	String fa_title;
	String fa_icon;

	String rf_detail; //편의시설 목록의 상세정보
}
