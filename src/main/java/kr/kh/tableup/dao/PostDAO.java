package kr.kh.tableup.dao;

import java.util.List;

import kr.kh.tableup.model.vo.BoardVO;
import kr.kh.tableup.model.vo.PostVO;

public interface PostDAO {

  List<PostVO> selectPostList(int bo_num);

  List<BoardVO> selectBoardList();
  
}
