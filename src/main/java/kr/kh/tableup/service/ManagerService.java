package kr.kh.tableup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.kh.tableup.dao.ManagerDAO;
import kr.kh.tableup.model.util.UploadFileUtils;
import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.RegionVO;
import kr.kh.tableup.model.vo.RestaurantManagerVO;
import kr.kh.tableup.model.vo.RestaurantVO;

@Service
public class ManagerService {
	
	@Autowired
	ManagerDAO managerDAO;

	@Value("${spring.path.upload}")
	String uploadPath;

	public boolean insertManager(RestaurantManagerVO rm) {
		return managerDAO.insertManager(rm);
	}

	public RestaurantManagerVO getManagerId(String rm_id) {
		
		return managerDAO.selectManager(rm_id);
	}

	public RestaurantVO selectRestaurant(int rm_num) {
		return managerDAO.selectRestaurant(rm_num);
	}

	public boolean insertRestaurant(RestaurantVO restaurant, RestaurantManagerVO manager, MultipartFile[] fileList) {
		if(restaurant == null || manager == null){
			return false;
		}
		boolean res=managerDAO.insertRestaurant(restaurant);
		if(!res){
			return false;
		}
		//매장 이미지 작업
		uploadFileList(restaurant.getRt_num(), fileList);
		
		//매장 정보 등록후 점주에게 매장 정보 부여
		return true;
	}


	//삽입한 사진수만큼 등록하는 작업
	private void uploadFileList(int rt_num, MultipartFile[] fileList) {
		if(fileList == null || fileList.length == 0){
			return;
		}
		for(MultipartFile file : fileList){
			uploadFile(rt_num,file);
		}
	}

	private void uploadFile(int rt_num, MultipartFile file) {
		if(file==null||file.getOriginalFilename().isEmpty()){
			return;
		}
		String fi_ori_name=file.getOriginalFilename();
		try{
			String fi_name=UploadFileUtils.uploadFile(uploadPath, fi_ori_name, file.getBytes());

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public List<FoodCategoryVO> getFoodCategory() {
		return managerDAO.selectFoodCategoryList();
	}

	public List<RegionVO> getRegion() {
	return managerDAO.selectRegionList();
	}

	public List<DetailRegionVO> getDetailRegion() {
		return managerDAO.selectDetailRegionList();
	}

	public List<DetailFoodCategoryVO> getDetailFood() {
	return managerDAO.selectDetailFoodList();
	}

	public List<DetailFoodCategoryVO> getDetailByFcNum(int fc_num) {
		return managerDAO.selectDetailByFcNum(fc_num);
	}

	public List<DetailRegionVO> getDetailByRegNum(int reg_num) {
		return managerDAO.selectDetailByRegNum(reg_num);
	}

}
