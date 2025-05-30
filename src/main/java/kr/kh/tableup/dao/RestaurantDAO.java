package kr.kh.tableup.dao;

import java.util.List;
import java.util.Map;


import org.apache.ibatis.annotations.Param;

import kr.kh.tableup.model.vo.RestaurantDetailVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.model.vo.ReviewVO;

public interface RestaurantDAO {
    // rt_num으로 식당 1개 가져오기
    RestaurantVO selectRestaurantByNum(int rt_num);

    List<Map<String, String>> selecrSearchRecommend(String keyword);
    
	List<RestaurantDetailVO> selectRestaurantDetailList(@Param("rt_num") int rt_num);

	List<ReviewVO> selectReviewListbyNum(@Param("rt_num") int rt_num);
}
