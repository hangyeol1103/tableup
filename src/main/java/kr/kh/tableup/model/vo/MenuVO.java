package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class MenuVO {
	
	int mn_num;
	String mn_name;
	String mn_img;
	int mn_price;
	String mn_content;
	int mn_mt_num;
	int mn_rt_num;
	String mn_div;

	private MenuTypeVO menuType;

	public void setMn_img(String mn_img) {
		this.mn_img = mn_img;
	}

	public String getMn_div() {
		return mn_div;
	}

}
