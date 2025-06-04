package kr.kh.tableup.model.vo;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BusinessHourVO {
	int bh_num;

	String bh_start; // "HH:mm"
	String bh_end;
	int bh_seat_max;
	int bh_seat_current;
	int bh_table_max;
	int bh_table_current;
	boolean bh_state;
	int bh_rt_num;

	String bh_date; // "yyyy-MM-dd"

	// DB 저장용 timestamp (자동 생성)

	Timestamp bh_start_ts;
	
	Timestamp bh_end_ts;

	// bh_date, bh_start, bh_end 셋 중 하나가 설정될 때마다 timestamp 자동 계산
	public void setBh_date(String bh_date) {
		this.bh_date = bh_date;
		updateTimestamps();
	}

	public void setBh_start(String bh_start) {
		this.bh_start = bh_start;
		updateTimestamps();
	}

	public void setBh_end(String bh_end) {
		this.bh_end = bh_end;
		updateTimestamps();
	}

	private void updateTimestamps() {
		if (this.bh_date != null && this.bh_start != null)
			this.bh_start_ts = combineToTimestamp(this.bh_date, this.bh_start);
		if (this.bh_date != null && this.bh_end != null)
			this.bh_end_ts = combineToTimestamp(this.bh_date, this.bh_end);
	}

	private Timestamp combineToTimestamp(String date, String time) {
		if (date == null || time == null || date.isBlank() || time.isBlank()) return null;
		try {
			return Timestamp.valueOf(date + " " + time + ":00");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
