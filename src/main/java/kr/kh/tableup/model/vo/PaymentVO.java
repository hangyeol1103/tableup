package kr.kh.tableup.model.vo;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class PaymentVO {
	int pay_num;
	int pay_res_num;
	String pay_method;
	int pay_amount;
	String pay_status;

	@DateTimeFormat(pattern = "HH:mm")
	LocalDateTime pay_time;

	ReservationVO reservation;
}
