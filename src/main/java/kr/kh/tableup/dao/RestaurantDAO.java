package kr.kh.tableup.dao;

import java.util.List;
import java.util.Map;


import org.apache.ibatis.annotations.Param;

import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.MenuTypeVO;
import kr.kh.tableup.model.vo.MenuVO;
import kr.kh.tableup.model.vo.ResNewsVO;
import kr.kh.tableup.model.vo.RestaurantDetailVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.model.vo.ReviewVO;

public interface RestaurantDAO {
    // rt_num으로 식당 1개 가져오기
    RestaurantVO selectRestaurantByNum(int rt_num);

    List<Map<String, String>> selecrSearchRecommend(String keyword);
    
	List<RestaurantDetailVO> selectRestaurantDetailList(@Param("rt_num") int rt_num);

	List<ReviewVO> selectReviewListbyNum(@Param("rt_num") int rt_num);

    double countScoreByRtNum(@Param("rt_num") int rt_num);

    int countReviewByRtNum(@Param("rt_num") int rt_num);
    
    // int countReviewByUsNum(int rt_num, int us_num);

		List<ResNewsVO> selectTapResNewsList(@Param("rt_num") int rt_num);

		// List<MenuVO> selectTapMenuList(@Param("rt_num") int rt_num);

		List<MenuTypeVO> selectMenuTypeList(@Param("rt_num") int rt_num);

		List<MenuVO> selectMenuDivList(@Param("rt_num") int rt_num);

		List<FileVO> selectTapFileList(@Param("rt_num") int rt_num);

}


