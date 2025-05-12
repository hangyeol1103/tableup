package kr.kh.tableup.dao;

import kr.kh.tableup.model.vo.RestaurantManagerVO;
import kr.kh.tableup.model.vo.RestaurantVO;

public interface ManagerDAO {

	boolean insertManager(RestaurantManagerVO rm);

	RestaurantManagerVO findById(String username);

	RestaurantManagerVO selectManager(String rm_id);

	RestaurantVO selectRestaurant(int rm_rum);

	boolean insertRestaurant(RestaurantVO restaurant);
	
}
