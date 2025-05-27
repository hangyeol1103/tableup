package kr.kh.tableup.model.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReservationVO {
	private int res_num;         // Reservation number (Primary Key, Auto Increment)
	private int res_us_num;       // User number
	private int res_rt_num;       // Restaurant number
	private LocalDateTime res_time;      // Reservation time
	private LocalDateTime res_end_time;
	private int res_person;      // Number of persons
	private String res_request;  // Special requests
	private LocalDateTime res_created;   // Creation time
	private int res_state;       // Reservation state

	private String us_name;
	private String rt_name;
}
