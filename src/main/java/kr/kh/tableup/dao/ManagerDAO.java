package kr.kh.tableup.dao;

import java.util.List;

import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.RegionVO;
import kr.kh.tableup.model.vo.RestaurantManagerVO;
import kr.kh.tableup.model.vo.RestaurantVO;

public interface ManagerDAO {

	boolean insertManager(RestaurantManagerVO rm);

	RestaurantManagerVO findById(String username);

	RestaurantManagerVO selectManager(String rm_id);

	RestaurantVO selectRestaurant(int rm_rum);

	boolean insertRestaurant(RestaurantVO restaurant);

	List<FoodCategoryVO> selectFoodCategoryList();

	List<RegionVO> selectRegionList();

	List<DetailFoodCategoryVO> selectDetailFoodList();

	List<DetailRegionVO> selectDetailRegionList();
	
}
