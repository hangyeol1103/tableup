package kr.kh.tableup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.kh.tableup.dao.FileDAO;

@Service
public class FileService {


	@Autowired
	FileDAO fileDAO;

  @Value("${spring.path.upload}")
  String uploadPath;

	public void insertFile(String rev_num, String file_name, String file_tag, MultipartFile file) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'insertFile'");
	}
	


}
