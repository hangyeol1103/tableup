package kr.kh.tableup.model.DTO;

import java.util.List;

import kr.kh.tableup.model.vo.ReviewScoreVO;
import kr.kh.tableup.model.vo.ReviewVO;
import lombok.Data;

@Data
public class ReviewDTO {
	private ReviewVO review;
	private List<ReviewScoreVO> scoreList;
	private List<FileDTO> fileList;
}
