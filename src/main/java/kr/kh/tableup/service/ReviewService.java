package kr.kh.tableup.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.ReviewDAO;
import kr.kh.tableup.model.vo.ReviewVO;

@Service
public class ReviewService {

	@Autowired
	ReviewDAO reviewDAO;
	


	public void insertReview(ReviewVO review, Map<String,String> scores) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'insertReview'");
	}


}
