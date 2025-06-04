package kr.kh.tableup.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.BusinessHourDAO;
import kr.kh.tableup.dao.ReservationDAO;
import kr.kh.tableup.dao.RestaurantDAO;
import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.MenuTypeVO;
import kr.kh.tableup.model.vo.MenuVO;
import kr.kh.tableup.model.vo.ResNewsVO;
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

	public List<Map<String, String>> getSearchRecommend(String keyword) {
		if(keyword == null || keyword.length() < 2) return List.of();
		
		return restaurantDAO.selecrSearchRecommend(keyword);
	}

	public List<RestaurantDetailVO> getRestaurantDetailList(int rt_num) {
		return restaurantDAO.selectRestaurantDetailList(rt_num);
	}

	public List<ReviewVO> getReviewList(int rt_num) {
		return restaurantDAO.selectReviewListbyNum(rt_num);
	}

	public double getCountScoreByRtNum(int rt_num) {
		return restaurantDAO.countScoreByRtNum(rt_num);
	}

	public int getCountReviewByRtNum(int rt_num) {
		return restaurantDAO.countReviewByRtNum(rt_num);
	}

	public List<ResNewsVO> getTapResNewsList(int rt_num) {
		return restaurantDAO.selectTapResNewsList(rt_num);
	}

	// public List<MenuVO> getTapMenuList(int rt_num) {
	// 	return restaurantDAO.selectTapMenuList(rt_num);
	// }

	public List<MenuTypeVO> getMenuTypeList(int rt_num) {
		return restaurantDAO.selectMenuTypeList(rt_num);
	}

	public List<MenuVO> getMenuDivList(int rt_num) {
		return restaurantDAO.selectMenuDivList(rt_num);
	}

	public List<FileVO> getTapFileList(int rt_num) {
		return restaurantDAO.selectTapFileList(rt_num);
	}

	
	
}
