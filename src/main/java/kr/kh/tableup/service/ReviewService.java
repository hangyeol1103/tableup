package kr.kh.tableup.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.FileDAO;
import kr.kh.tableup.dao.ReservationDAO;
import kr.kh.tableup.dao.ReviewDAO;
import kr.kh.tableup.dao.UserDAO;
import kr.kh.tableup.model.DTO.FileDTO;
import kr.kh.tableup.model.DTO.ReviewDTO;
import kr.kh.tableup.model.util.UploadFileUtils;
import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.ReviewScoreVO;
import kr.kh.tableup.model.vo.ReviewVO;
import kr.kh.tableup.model.vo.UserVO;

@Service
public class ReviewService {

	@Autowired
	ReviewDAO reviewDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	FileDAO fileDAO;

	@Autowired
	ReservationDAO reservationDAO;

  @Value("${spring.path.upload}")
  String uploadPath;
	
	private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

/*
	public void insertReview(ReviewDTO reviewDTO) {

		// 리뷰 등록
		ReviewVO review = reviewDTO.getReview();
		List<ReviewScoreVO> scoreList = reviewDTO.getScoreList();
		List<FileDTO> fileList = reviewDTO.getFileList();

		int rev_num = 0;
		int[] rs_num = new int[scoreList.size()];
		

		System.out.println(review);
		String reviewError = "";
		if (review.getRev_rt_num() <= 0 || review.getRev_visit() == null || review.getRev_visitor() <= 0 || review.getRev_us_num() < 1 ) {
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
			rev_num = reviewDAO.insertReview(review) ? review.getRev_num() : 0;
			if(rev_num < 1) throw new RuntimeException("리뷰 등록에 실패했습니다.");
		} catch (DataAccessException e) {
				Throwable rootCause = NestedExceptionUtils.getRootCause(e); 
				if (rootCause != null && rootCause.getMessage().contains("이미 이 예약에 대한 리뷰가 존재합니다")) {
						throw new RuntimeException("이미 작성한 리뷰가 존재합니다.");
				} else {
						throw new RuntimeException(rootCause != null ? rootCause.getMessage() : "알 수 없는 DB 오류가 발생했습니다.");
				}
		}


		// 리뷰 점수 등록
		try{
			for(int i = 0; i<scoreList.size(); i++){	// map 말고 리뷰스코어vo 쓸건데 일단 우선 맵으로 
				ReviewScoreVO score = scoreList.get(i);
				if(score.getRs_score()>5||score.getRs_score()<1) throw new RuntimeException("점수가 범위 밖입니다");
				
				score.setRs_rev_num(rev_num);
				score.setRs_st_num(score.getSt_num());
				rs_num[i] = reviewDAO.insertReviewScore(scoreList.get(i));
			}
		}catch(RuntimeException e){
			reviewDAO.deleteReview(rev_num);
			for(int i = 0; i<rs_num.length;i++ )reviewDAO.deleteReviewScore(rs_num[i]);
			throw new RuntimeException(e.getMessage()+"점수 저장에 실패했습니다.");
		}

		// 파일 업로드
		List<String> uploadedPaths = new ArrayList<>();
		int[] fileNum = new int[(fileList != null) ? fileList.size() : 0];
		if (fileList != null && !fileList.isEmpty()) {
			for (int i = 0; i < fileList.size(); i++) {
				FileDTO fileDTO = fileList.get(i);
				if (fileDTO == null || fileDTO.getUploadFile() == null || fileDTO.getUploadFile().isEmpty()) continue;

				try {
					String filePath = UploadFileUtils.uploadFile(uploadPath, fileDTO.getUploadFile().getOriginalFilename(), fileDTO.getUploadFile().getBytes());
					uploadedPaths.add(filePath);

					FileVO fileVO = new FileVO();
					fileVO.setFile_path(filePath);
					fileVO.setFile_name(fileDTO.getFile_name());
					fileVO.setFile_type("REVIEW");
					fileVO.setFile_foreign(rev_num);
					fileVO.setFile_tag(fileDTO.getFile_tag());
					fileVO.setFile_res_num(review.getRev_rt_num());

					fileNum[i] = fileDAO.insertFile(fileVO);
				} catch (Exception e) {
					System.out.println("파일 업로드 실패: " + e.getMessage());
					for (String path : uploadedPaths) UploadFileUtils.delteFile(uploadPath, path);
					reviewDAO.deleteReview(rev_num);
					for (int j = 0; j < rs_num.length; j++) reviewDAO.deleteReviewScore(rs_num[j]);
					for (int j = 0; j < fileNum.length; j++) if (fileNum[j] > 0) fileDAO.deleteFile(fileNum[j]);
					throw new RuntimeException("첨부파일 업로드 실패: " + e.getMessage());
				}
			}
		}

	




	}*/


		public void insertReview(ReviewDTO reviewDTO) {
			ReviewVO review = reviewDTO.getReview();
			List<ReviewScoreVO> scoreList = reviewDTO.getScoreList();
			List<FileDTO> fileList = reviewDTO.getFileList();


			StringBuilder reviewError = new StringBuilder();
			if (review.getRev_rt_num() <= 0) reviewError.append("매장이 선택되지 않았습니다.\n");
			if (review.getRev_us_num() <= 0 || userDAO.selectUser(review.getRev_us_num()) == null) 
					reviewError.append("유저 정보가 없습니다.\n");
			if (review.getRev_visit() == null) review.setRev_visit(LocalDate.now().toString());
			if (review.getRev_visitor() <= 0) reviewError.append("방문 인원이 없습니다.\n");
			if (review.getRev_content() == null || review.getRev_content().trim().isEmpty()) 
					review.setRev_content("내용 없음");
			if (reviewError.length() > 0) throw new RuntimeException(reviewError.toString());

			int rev_num = 0;
			try {
					boolean inserted = reviewDAO.insertReview(review);
					if (!inserted || review.getRev_num() <= 0) throw new RuntimeException("리뷰 등록 실패");
					rev_num = review.getRev_num();
			} catch (DataAccessException e) {
					Throwable root = NestedExceptionUtils.getRootCause(e);
					String message = (root != null) ? root.getMessage() : "DB 오류";
					if (message.contains("uq_resnum")) {
							throw new RuntimeException("해당 예약에 리뷰가 존재합니다.");
					}
					throw new RuntimeException("리뷰 저장 중 오류: " + message);
			}

			//reves rt us rev res
			if (!reviewDAO.insertRevres(review.getRev_rt_num(),review.getRev_us_num(),rev_num,review.getRes_num())) {
					reviewDAO.deleteReview(rev_num);
					throw new RuntimeException("DB 예약-리뷰 연결 실패");
			}

			int[] rs_num = new int[scoreList.size()];
			try {
					for (int i = 0; i < scoreList.size(); i++) {
							ReviewScoreVO score = scoreList.get(i);
							if (score.getRs_score() < 1 || score.getRs_score() > 5)
									throw new RuntimeException("1~5를 벗어난 점수를 입력받았습니다.");
							score.setRs_rev_num(rev_num);
							score.setRs_st_num(score.getSt_num());
							rs_num[i] = reviewDAO.insertReviewScore(score);
					}
			} catch (RuntimeException e) {
					reviewDAO.deleteReview(rev_num);
					for (int rs : rs_num) reviewDAO.deleteReviewScore(rs);
					throw new RuntimeException("평점 저장 실패: " + e.getMessage());
			}

			List<String> uploadedPaths = new ArrayList<>();
			int[] fileNum = new int[fileList != null ? fileList.size() : 0];
			if (fileList != null && !fileList.isEmpty()) {
					for (int i = 0; i < fileList.size(); i++) {
							FileDTO fileDTO = fileList.get(i);
							if (fileDTO == null || fileDTO.getUploadFile() == null || fileDTO.getUploadFile().isEmpty()) continue;
							try {
									String filePath = UploadFileUtils.uploadFile(uploadPath, fileDTO.getUploadFile().getOriginalFilename(), fileDTO.getUploadFile().getBytes());
									uploadedPaths.add(filePath);

									FileVO fileVO = new FileVO();
									fileVO.setFile_path(filePath);
									fileVO.setFile_name(fileDTO.getFile_name());
									fileVO.setFile_type("REVIEW");
									fileVO.setFile_foreign(rev_num);
									fileVO.setFile_tag(fileDTO.getFile_tag());
									fileVO.setFile_res_num(review.getRev_rt_num());

									fileNum[i] = fileDAO.insertFile(fileVO);
							} catch (Exception e) {
									for (String path : uploadedPaths) UploadFileUtils.delteFile(uploadPath, path);
									reviewDAO.deleteReview(rev_num);
									for (int rs : rs_num) reviewDAO.deleteReviewScore(rs);
									for (int fn : fileNum) if (fn > 0) fileDAO.deleteFile(fn);
									throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
							}
					}
			}
		}



	public void insertReview(ReviewVO review, Map<String,String> scores) {
		


		throw new RuntimeException(); //첨부파일 없을시 리뷰와 점수만으로 등록하고 예외발생으로 탈출
	}


	

	
  private String getSuffix(String fileName) {
    int index = fileName.lastIndexOf(".");
    return index < 0 ? null : fileName.substring(index);
  }


	public ReviewVO getReviewByReservation(int res_num) {
		
		return reviewDAO.selectReviewByReservation(res_num);
	}


	public ReviewVO getReview(int rev_num) {
		
		return reviewDAO.selectReview(rev_num);
	}


	public int revres(int res_num, int us_num) {
		ReservationVO reservation = reservationDAO.selectReservation(res_num);
    if (reservation == null || reservation.getRes_us_num() != us_num) {
        throw new IllegalStateException("해당 예약을 찾을 수 없습니다.");
    }

    ReviewVO review = reviewDAO.selectReviewByReservation(res_num);
    if (review == null || review.getRev_num() < 0) {
			return res_num; // 이 res_num 그대로 insert 페이지 URL에 사용
		}
		
		throw new IllegalStateException("이미 리뷰를 작성한 예약입니다.");
}

/*/

	public int insertReviewAndScore(ReviewDTO reviewDTO) {
			// 리뷰 등록
		ReviewVO review = reviewDTO.getReview();
		List<ReviewScoreVO> scoreList = reviewDTO.getScoreList();
		List<FileDTO> fileList = reviewDTO.getFileList();

		int rev_num = 0;
		int[] rs_num = new int[scoreList.size()];
		

		System.out.println(review);
		String reviewError = "";
		if (review.getRev_rt_num() <= 0 || review.getRev_visit() == null || review.getRev_visitor() <= 0 || review.getRev_us_num() < 1 ) {
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
		System.out.println("저기까진 왔습니다.");
		try{
			System.out.println("여기까진 왔습니다.");
			rev_num = reviewDAO.insertReview(review) ? review.getRev_num() : 0;
			if(rev_num < 1) throw new RuntimeException("리뷰 등록에 실패했습니다.");
		} catch (DataAccessException e) {
				Throwable rootCause = NestedExceptionUtils.getRootCause(e); 
				if (rootCause != null && rootCause.getMessage().contains("이미 이 예약에 대한 리뷰가 존재합니다")) {
						throw new RuntimeException("이미 작성한 리뷰가 존재합니다.");
				} else {
						throw new RuntimeException("알 수 없는 DB 오류가 발생했습니다.");
				}


		}





		// 리뷰 점수 등록
		try{
			for(int i = 0; i<scoreList.size(); i++){	// map 말고 리뷰스코어vo 쓸건데 일단 우선 맵으로 
				int score = scoreList.get(i).getRs_score();
				if (score < 1 || score > 5) throw new RuntimeException("점수는 1~5 사이여야 합니다.");
				scoreList.get(i).setRs_rev_num(rev_num);
				rs_num[i] = reviewDAO.insertReviewScore(scoreList.get(i));
			}
		}catch(RuntimeException e){
			reviewDAO.deleteReview(rev_num);
			for(int i = 0; i<rs_num.length;i++ )reviewDAO.deleteReviewScore(rs_num[i]);
			throw new RuntimeException("점수 저장에 실패했습니다.");
		}

		return rev_num;
	}
*/
}
