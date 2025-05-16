package kr.kh.tableup.model.vo;

import java.util.Date;
import lombok.Data;

@Data
public class ReviewVO {
	private int rev_num;
	private int rev_us_num;
	private int rev_rt_num;
	private String rev_content;
	private Date rev_created;
	private Date rev_updated;
	private int rev_state;
	private String rev_visit;
	private int rev_visitor;

	private String us_name;
	private String rt_name;
}
