package kr.kh.tableup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.kh.tableup.dao.ManagerDAO;
import kr.kh.tableup.model.vo.RestaurantManagerVO;
import kr.kh.tableup.model.vo.RestaurantVO;

@Service
public class ManagerService {
	
	@Autowired
	ManagerDAO managerDAO;

	public boolean insertManager(RestaurantManagerVO rm) {
		return managerDAO.insertManager(rm);
	}

	public RestaurantManagerVO getManagerId(String rm_id) {
		
		return managerDAO.selectManager(rm_id);
	}

	public RestaurantVO selectRestaurant(int rm_num) {
		return managerDAO.selectRestaurant(rm_num);
	}

	public boolean insertRestaurant(RestaurantVO restaurant, RestaurantManagerVO manager, MultipartFile file) {
		if(restaurant == null){
			return false;
		}
		boolean res=managerDAO.insertRestaurant(restaurant);
		if(!res){
			return false;
		}
		//매장 이미지 작업
		String fileName = file.getOriginalFilename();
		String suffix =getSuffix(fileName);
		String newFileName = restaurant.getRt_num() + suffix;
		String resimage;
		try{
			

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	private String getSuffix(String fileName) {
		int index = fileName.lastIndexOf(".");
		return index<0 ? null : fileName.substring(index);
	}
}
