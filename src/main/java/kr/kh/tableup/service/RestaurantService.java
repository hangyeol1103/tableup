package kr.kh.tableup.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.BusinessHourDAO;
import kr.kh.tableup.dao.ReservationDAO;
import kr.kh.tableup.dao.RestaurantDAO;
import kr.kh.tableup.model.vo.BusinessDateVO;
import kr.kh.tableup.model.vo.BusinessHourVO;
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

	public List<BusinessHourVO> getBusinessHour(int rt_num) {

		List<BusinessHourVO> businessHourList = restaurantDAO.selectBusinessHour(rt_num);
		if(businessHourList == null) return null;

		for(BusinessHourVO businessHour : businessHourList){
			businessHour.setBh_seat_remain(businessHour.getBh_seat_max()-businessHour.getBh_seat_current());
			businessHour.setBh_table_remain(businessHour.getBh_table_max()-businessHour.getBh_table_current());
		}

		return businessHourList;
	}


	public Map<String, Integer> remain(int rt_num) {
    List<BusinessHourVO> list = getBusinessHour(rt_num);
		
		Map<String, Integer> result = new TreeMap<>();
    for (BusinessHourVO hourVO : list) {
			String dateKey = hourVO.getBh_start().toLocalDate().toString(); 
			int remain = hourVO.getBh_seat_max() - hourVO.getBh_seat_current();
			result.put(dateKey, result.getOrDefault(dateKey, 0) + remain);
    }
    return result;
}

	public List<BusinessDateVO> getBusinessDate(int rt_num) {
		return restaurantDAO.selectBusinessDate(rt_num);
	}

	
	
}
