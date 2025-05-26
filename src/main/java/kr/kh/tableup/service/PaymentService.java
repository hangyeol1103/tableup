package kr.kh.tableup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.PaymentDAO;
import kr.kh.tableup.model.vo.PaymentVO;

@Service
public class PaymentService {
	
	@Autowired
	PaymentDAO paymentDAO;

	public List<PaymentVO> getPaymentList(int rt_num) {
		return paymentDAO.selectPaymentList(rt_num);
	}
}
