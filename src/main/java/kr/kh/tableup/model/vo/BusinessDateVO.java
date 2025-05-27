package kr.kh.tableup.model.vo;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessDateVO {

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
	@JsonIgnore
	private Timestamp  bd_open_ts;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@JsonIgnore
	private Timestamp  bd_close_ts;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@JsonIgnore
	private Timestamp  bd_brstart_ts;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@JsonIgnore
	private Timestamp  bd_brend_ts;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@JsonIgnore
	private Timestamp  bd_loam_ts;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@JsonIgnore
	private Timestamp  bd_lopm_ts;



/////////////////////////////////////////////

	@JsonIgnore
	private String  bd_local_date;

	
	// public String getBd_date(){
	// 	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	// 	return format.format(bd_open);
	// }

	
	public BusinessDateVO(String bd_date, int bd_rt_num, boolean bd_off,
												String bd_open, String bd_close,
												String bd_brstart, String bd_brend,
												String bd_loam, String bd_lopm) {

			this.bd_date = bd_date;
			this.bd_rt_num = bd_rt_num;
			this.bd_off = bd_off;

			this.bd_open = bd_open;
			this.bd_close = bd_close;
			this.bd_brstart = bd_brstart;
			this.bd_brend = bd_brend;
			this.bd_loam = bd_loam;
			this.bd_lopm = bd_lopm;

			// Timestamp 생성
			this.bd_open_ts    = combineToTimestamp(bd_date, bd_open);
			this.bd_close_ts   = combineToTimestamp(bd_date, bd_close);
			this.bd_brstart_ts = combineToTimestamp(bd_date, bd_brstart);
			this.bd_brend_ts   = combineToTimestamp(bd_date, bd_brend);
			this.bd_loam_ts    = combineToTimestamp(bd_date, bd_loam);
			this.bd_lopm_ts    = combineToTimestamp(bd_date, bd_lopm);
	}

	private Timestamp combineToTimestamp(String date, String time) {
			if (date == null || time == null || time.isBlank()) return null;
			try {
					return Timestamp.valueOf(date + " " + time + ":00");
			} catch (Exception e) {
					return null; // 혹은 예외 처리
			}
	}

	public void setBd_open(String bd_open) {
    this.bd_open = bd_open;
    if (this.bd_date != null && bd_open != null) {
        this.bd_open_ts = combineToTimestamp(this.bd_date, bd_open);
    }
	}
	public void setBd_close(String bd_close) {
			this.bd_close = bd_close;
			if (this.bd_date != null && bd_close != null) {
					this.bd_close_ts = combineToTimestamp(this.bd_date, bd_close);
			}
		}
	public void setBd_brstart(String bd_brstart) {
			this.bd_brstart = bd_brstart;
			if (this.bd_date != null && bd_brstart != null) {
					this.bd_brstart_ts = combineToTimestamp(this.bd_date, bd_brstart);
			}
		}
	public void setBd_brend(String bd_brend) {
			this.bd_brend = bd_brend;
			if (this.bd_date != null && bd_brend != null) {
					this.bd_brend_ts = combineToTimestamp(this.bd_date, bd_brend);
			}
		}
	public void setBd_loam(String bd_loam) {
			this.bd_loam = bd_loam;
			if (this.bd_date != null && bd_loam != null) {
					this.bd_loam_ts = combineToTimestamp(this.bd_date, bd_loam);
			}
		}
	public void setBd_lopm(String bd_lopm) {
			this.bd_lopm = bd_lopm;
			if (this.bd_date != null && bd_lopm != null) {
					this.bd_lopm_ts = combineToTimestamp(this.bd_date, bd_lopm);
			}
		}
	public void setBd_date(String bd_date) {
			this.bd_date = bd_date;
			if (bd_date != null) {
					this.bd_local_date = bd_date; // 로컬 날짜로 저장
					// Timestamp 업데이트
					if (this.bd_open != null) {
							this.bd_open_ts = combineToTimestamp(bd_date, this.bd_open);
					}
					if (this.bd_close != null) {
							this.bd_close_ts = combineToTimestamp(bd_date, this.bd_close);
					}
					if (this.bd_brstart != null) {
							this.bd_brstart_ts = combineToTimestamp(bd_date, this.bd_brstart);
					}
					if (this.bd_brend != null) {
							this.bd_brend_ts = combineToTimestamp(bd_date, this.bd_brend);
					}
					if (this.bd_loam != null) {
							this.bd_loam_ts = combineToTimestamp(bd_date, this.bd_loam);
					}
					if (this.bd_lopm != null) {
							this.bd_lopm_ts = combineToTimestamp(bd_date, this.bd_lopm);
					}
			}
		}


}
