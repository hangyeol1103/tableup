package kr.kh.tableup.dao;

import java.util.List;
import java.util.Map;

import kr.kh.tableup.model.vo.RestaurantVO;

public interface RestaurantDAO {
    // rt_num으로 식당 1개 가져오기
    RestaurantVO selectRestaurantByNum(int rt_num);

    List<Map<String, String>> selecrSearchRecommend(String keyword);
}
