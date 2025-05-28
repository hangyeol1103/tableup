package kr.kh.tableup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.BusinessHourDAO;
import kr.kh.tableup.dao.ReservationDAO;
import kr.kh.tableup.dao.RestaurantDAO;

@Service
public class RestaurantService {

	@Autowired
	ReservationDAO reservationDAO;

	@Autowired
	BusinessHourDAO businessHourDAO;

	@Autowired
	RestaurantDAO restaurantDAO;

	
	
}
