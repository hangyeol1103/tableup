package kr.kh.tableup.dao;

import org.apache.ibatis.annotations.Param;

import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.ReviewScoreVO;
import kr.kh.tableup.model.vo.ReviewVO;

public interface ReviewDAO {

	Boolean insertReview(ReviewVO review);

	void deleteReview(int rev_num);

	int insertReviewScore(ReviewScoreVO reviewScoreVO);

	void deleteReviewScore(int rs_num);

	ReviewVO selectReviewByReservation(Integer res_num);

	ReviewVO selectReview(int rev_num);

	boolean insertRevres(@Param("rt_num") int rev_rt_num, @Param("us_num") int rev_us_num, @Param("rev_num") int rev_num, @Param("res_num") int res_num);

	//int insertFile(FileVO fileVO);

	

	
}
