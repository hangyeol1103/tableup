package kr.kh.tableup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.MenuTypeVO;
import kr.kh.tableup.model.vo.MenuVO;
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

	List<DetailFoodCategoryVO> selectDetailByFcNum(int fc_num);

	List<DetailRegionVO> selectDetailByRegNum(int reg_num);

	boolean updateManagerRtNum(@Param("rm_num")int rm_num,@Param("rt_num") int rt_num);

	List<MenuVO> selectMenuList(int rt_num);

	List<MenuTypeVO> selectMenuTypeList();

	boolean insertMenu(MenuVO menu);

	boolean updateMenu(MenuVO menu);

	MenuVO selectMenu(int mn_num);

	MenuTypeVO selectMenuType(int mn_mt_num);

	boolean deleteMenu(int mn_num);

	
}
