package kr.kh.tableup.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVO {
	int file_num;
	String file_path;
	String file_name;
	String file_type;
	int file_foreign;
	String file_tag;
	
	int file_res_num;

}
