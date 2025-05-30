package kr.kh.tableup.model.DTO;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FileDTO {
	int file_num;
	String file_path;
	String file_name;
	String file_type;
	String file_foreign;
	String file_tag;
	
	int file_res_num;

	MultipartFile file;
}
