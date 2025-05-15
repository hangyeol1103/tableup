package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class ReviewVO {
	int rev_num;
	int rev_us_num;
	int rev_rt_num;
	String rev_content;
	java.time.LocalDateTime rev_created;
	java.time.LocalDateTime rev_updated;
	int rev_state;
	java.time.LocalDateTime rev_visit;
	int rev_visitor;
}
