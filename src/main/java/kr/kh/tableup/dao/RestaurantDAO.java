package kr.kh.tableup.dao;

import kr.kh.tableup.model.vo.RestaurantVO;

public interface RestaurantDAO {
    // rt_num으로 식당 1개 가져오기
    RestaurantVO selectRestaurantByNum(int rt_num);
}
