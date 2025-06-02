package kr.kh.tableup.dao;

import kr.kh.tableup.model.vo.ReviewVO;

public interface ReviewDAO {

	Boolean insertReview(ReviewVO review);

	void deleteReview(int rev_num);

	

	
}
