package kr.kh.tableup.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.ReviewDAO;
import kr.kh.tableup.dao.UserDAO;
import kr.kh.tableup.model.DTO.FileDTO;
import kr.kh.tableup.model.vo.ReviewScoreVO;
import kr.kh.tableup.model.vo.ReviewVO;
import kr.kh.tableup.model.vo.UserVO;

@Service
public class ReviewService {

	@Autowired
	ReviewDAO reviewDAO;

	@Autowired
	UserDAO userDAO;
	


	public void insertReview(ReviewVO review, List<ReviewScoreVO> scores, List<FileDTO> fileList) {

		// 리뷰 등록
		System.out.println(review);
		String reviewError = "";
		if (review.getRev_rt_num() <= 0 || review.getRev_visit() == null || review.getRev_visitor() <= 0
			|| review.getRev_us_num() < 1 ) {
			reviewError += "입력 정보에 누락이 있어 리뷰 등록에 실패했습니다. \n";
    }
		if(review.getRev_rt_num() <= 0 ) reviewError += "매장이 선택되지 않았습니다. \n";
		if(review.getRev_visit() == null) review.setRev_visit(LocalDate.now().toString());			//날짜 없으면 일단 오늘로
		if(review.getRev_visitor() <= 0) reviewError += "인원수가 없습니다.\n";
		if(review.getRev_us_num() <= 0) reviewError += "현재 유저 정보를 찾을 수 없습니다.\n";
		else if((UserVO)userDAO.selectUser(review.getRev_us_num()) == null) reviewError += "해당하는 유저 정보를 찾을 수 없습니다.\n";
		if(review.getRev_content() == null) review.setRev_content("내용 없음");
		review.setRev_content(review.getRev_content().trim());

		if(reviewError.length()>0) throw new RuntimeException(reviewError);
		try{
			int rev_num = reviewDAO.insertReview(review) ? review.getRev_num() : 0;
			if(rev_num < 1) throw new RuntimeException("리뷰 등록에 실패했습니다.");
		} catch (DataAccessException e) {
    Throwable rootCause = NestedExceptionUtils.getRootCause(e); 
    if (rootCause != null && rootCause.getMessage().contains("이미 이 예약에 대한 리뷰가 존재합니다")) {
        throw new RuntimeException("이미 작성한 리뷰가 존재합니다.");
    } else {
        throw new RuntimeException("알 수 없는 DB 오류가 발생했습니다.");
    }
}


/*


		// 리뷰 점수 등록
		for(int i = 1; i<=scores.size();i++){	// map 말고 리뷰스코어vo 쓸건데 일단 우선 맵으로 

			if (review == null || rev_num < 1 || scores.get("score["+i+"]") < 1 || rs_score < 1 || rs_score > 5)
			int rs_num = reviewDAO.insertReviewScore(review.getRev_num(), scores);
		}

		// 파일 업로드
		int[] fileNum = new int[fileList.size()];	//파일리스트 말고 파일dto 쓸건데 일단 우선 리스트로
		if (fileList != null && !fileList.isEmpty()) {
			for (int i = 0; i < fileList.size(); i++) {
				if (!fileList.get(i).isEmpty()) {
					FileVO file = new FileVO(null, null, null, null, null, rs_num);
					fileNum[i] = reviewDAO.insertReviewFile(review.getRev_num(), fileList.get(i), fileNames.get(i), fileTags.get(i));

				}else continue;

			}
		}

		if(rev_num > 0 && rs_num > 0 && fileNum.length > 0) {
			reviewDAO.updateReviewFileForeign(review.getRev_num(), fileNum);
		}
		else {
			
			
			throw new RuntimeException("리뷰 등록에 실패했습니다.");
		}
*/


	}



	public void insertReview(ReviewVO review, Map<String,String> scores) {
		


		throw new RuntimeException(); //첨부파일 없을시 리뷰와 점수만으로 등록하고 예외발생으로 탈출
	}


}
