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
}
