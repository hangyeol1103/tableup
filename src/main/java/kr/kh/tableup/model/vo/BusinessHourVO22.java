package kr.kh.tableup.model.vo;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessHourVO22 {
	
	private int bh_num;
	
	private int bh_seat_max;
	private int bh_seat_current;
	private int bh_table_max;
	private int bh_table_current;
	private boolean bh_state;
	private int bh_rt_num;


	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private String bh_start;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private String bh_end;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private Timestamp bh_start_ts;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private Timestamp bh_end_ts;

	
	private String bh_date;
	@JsonIgnore
	private String bh_local_date;

	public BusinessHourVO22(int bh_seat_max, int bh_table_max,  boolean bh_state, int bh_rt_num,
	 											String bh_start, String bh_end, String bh_date){
		   this.bh_seat_max=bh_seat_max;
	  	 this.bh_table_max = bh_table_max;
	  	 this.bh_state = bh_state;
	  	 this.bh_rt_num = bh_rt_num;
   	   this.bh_start = bh_start;
   	   this.bh_end = bh_end;
			 this.bh_date = bh_date;

			 this.bh_start_ts = combineToTimestamp(bh_date, bh_start);
   	   this.bh_end_ts = combineToTimestamp(bh_date, bh_end);
	 }

	 private Timestamp combineToTimestamp(String date, String time) {
			if (date == null || time == null || time.isBlank()) return null;
			try {
					return Timestamp.valueOf(date + " " + time + ":00");
			} catch (Exception e) {
					return null; // 혹은 예외 처리
			}
	}

	public void setBh_start(String bh_start) {
    this.bh_start = bh_start;
    if (this.bh_date != null && bh_start != null) {
        this.bh_start_ts = combineToTimestamp(this.bh_date, bh_start);
    }
	}
	public void setBh_end(String bh_end) {
    this.bh_end = bh_end;
    if (this.bh_date != null && bh_end != null) {
        this.bh_end_ts = combineToTimestamp(this.bh_date, bh_end);
    }
	}
	public void setBh_date(String bh_date) {
    this.bh_date = bh_date;
    if (this.bh_start != null) {
        this.bh_start_ts = combineToTimestamp(bh_date, this.bh_start);
    }
    if (this.bh_end != null) {
        this.bh_end_ts = combineToTimestamp(bh_date, this.bh_end);
    }
}
}
