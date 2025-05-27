package kr.kh.tableup.dao;

import java.util.List;

import kr.kh.tableup.model.vo.PaymentVO;

public interface PaymentDAO {

	List<PaymentVO> selectPaymentList(int rt_num);
	
}
