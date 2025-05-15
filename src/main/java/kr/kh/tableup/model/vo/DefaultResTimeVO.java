package kr.kh.tableup.model.vo;

import lombok.Data;

@Data
public class DefaultResTimeVO {
	int dtr_num;
	int dtr_rt_num;
	String drt_date;
	boolean dtr_off;
	String dtr_open;
	String dtr_close;
	String dtr_brstart;
	String dtr_brend;
	String dtr_loam;
	String dtr_lopm;
}
