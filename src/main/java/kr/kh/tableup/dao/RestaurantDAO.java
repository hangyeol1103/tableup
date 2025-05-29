package kr.kh.tableup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.kh.tableup.model.vo.RestaurantDetailVO;
import kr.kh.tableup.model.vo.RestaurantVO;

public interface RestaurantDAO {
    // rt_num으로 식당 1개 가져오기
    RestaurantVO selectRestaurantByNum(int rt_num);

	List<RestaurantDetailVO> selectRestaurantDetailList(@Param("rt_num") int rt_num);
}
