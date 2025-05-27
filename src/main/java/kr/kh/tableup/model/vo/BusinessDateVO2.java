package kr.kh.tableup.model.vo;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessDateVO2 {

	private int bd_num;

	private String bd_date;
	private int bd_rt_num;
	private boolean bd_off;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private String  bd_open;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private String  bd_close;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private String  bd_brstart;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private String  bd_brend;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private String  bd_loam;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private String  bd_lopm;
	

		// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private Timestamp  bd_open_ts;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private Timestamp  bd_close_ts;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private Timestamp  bd_brstart_ts;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private Timestamp  bd_brend_ts;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private Timestamp  bd_loam_ts;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private Timestamp  bd_lopm_ts;



/////////////////////////////////////////////


	private String  bd_local_date;

	
	// public String getBd_date(){
	// 	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	// 	return format.format(bd_open);
	// }

	
}
