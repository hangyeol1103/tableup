package kr.kh.tableup.dao;

import kr.kh.tableup.model.vo.FileVO;

public interface FileDAO {

	int insertFile(FileVO fileVO);

	void deleteFile(int file_num);

	
}
