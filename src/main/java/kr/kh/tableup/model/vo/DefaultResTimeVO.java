package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class DefaultResTimeVO {
	int drt_num;
	int drt_rt_num;
	String drt_date;
	boolean drt_off;
	String drt_open;
	String drt_close;
	String drt_brstart;
	String drt_brend;
	String drt_loam;
	String drt_lopm;
}
