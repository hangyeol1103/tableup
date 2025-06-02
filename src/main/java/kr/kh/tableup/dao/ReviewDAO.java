package kr.kh.tableup.dao;

import kr.kh.tableup.model.DTO.FileDTO;
import kr.kh.tableup.model.vo.ReviewScoreVO;
import kr.kh.tableup.model.vo.ReviewVO;

public interface ReviewDAO {

	Boolean insertReview(ReviewVO review);

	void deleteReview(int rev_num);

	int insertReviewScore(ReviewScoreVO reviewScoreVO);

	void deleteReviewScore(int rs_num);

	int insertFile(FileDTO fileDTO);

	

	
}
