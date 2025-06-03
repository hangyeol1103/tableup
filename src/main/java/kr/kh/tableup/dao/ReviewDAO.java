package kr.kh.tableup.dao;

import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.ReviewScoreVO;
import kr.kh.tableup.model.vo.ReviewVO;

public interface ReviewDAO {

	Boolean insertReview(ReviewVO review);

	void deleteReview(int rev_num);

	int insertReviewScore(ReviewScoreVO reviewScoreVO);

	void deleteReviewScore(int rs_num);

	ReviewVO selectReview(Integer res_num);

	//int insertFile(FileVO fileVO);

	

	
}
