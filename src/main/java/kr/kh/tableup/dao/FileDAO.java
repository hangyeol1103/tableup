package kr.kh.tableup.dao;

import kr.kh.tableup.model.vo.FileVO;

public interface FileDAO {

	int insertFile(FileVO fileVO);

	boolean deleteFile(int file_num);

	
}
