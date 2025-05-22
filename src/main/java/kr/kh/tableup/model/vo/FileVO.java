package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class FileVO {
	int file_num;
	String file_path;
	String file_name;
	String file_type;
	String file_foreign;
	String file_tag;
	
	int file_res_num;

	public FileVO(String file_path, String file_name, 
								String file_type, String file_foreign,
								String file_tag, int file_res_num){
		this.file_path = file_path;
		this.file_name = file_name;
		this.file_type = file_type;
		this.file_foreign = file_foreign;
		this.file_tag = file_tag;
		this.file_res_num = file_res_num;
	}
}
