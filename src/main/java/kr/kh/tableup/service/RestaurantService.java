package kr.kh.tableup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.BusinessHourDAO;
import kr.kh.tableup.dao.ReservationDAO;
import kr.kh.tableup.dao.RestaurantDAO;
import kr.kh.tableup.model.vo.RestaurantDetailVO;
import kr.kh.tableup.model.vo.ReviewVO;

@Service
public class RestaurantService {

	@Autowired
	ReservationDAO reservationDAO;

	@Autowired
	BusinessHourDAO businessHourDAO;

	@Autowired
	RestaurantDAO restaurantDAO;

	public List<RestaurantDetailVO> getRestaurantDetailList(int rt_num) {
		return restaurantDAO.selectRestaurantDetailList(rt_num);
	}

	public List<ReviewVO> getReviewList(int rt_num) {
		return restaurantDAO.selectReviewListbyNum(rt_num);
	}

	
	
}
