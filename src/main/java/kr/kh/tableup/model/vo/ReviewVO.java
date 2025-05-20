package kr.kh.tableup.model.vo;

import java.util.Date;
import java.util.List;

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

	//작성한 유저(userVO)
	private String us_name;
	//연결된 레스토랑(restaurantVO)
	private String rt_name;

	private List<ReviewScoreVO> scoreList;
}
