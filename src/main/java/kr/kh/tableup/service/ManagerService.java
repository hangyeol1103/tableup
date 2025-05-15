package kr.kh.tableup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.kh.tableup.dao.ManagerDAO;
import kr.kh.tableup.model.util.UploadFileUtils;
import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.MenuTypeVO;
import kr.kh.tableup.model.vo.MenuVO;
import kr.kh.tableup.model.vo.RegionVO;
import kr.kh.tableup.model.vo.ResCouponVO;
import kr.kh.tableup.model.vo.ResNewsVO;
import kr.kh.tableup.model.vo.RestaurantDetailVO;
import kr.kh.tableup.model.vo.RestaurantManagerVO;
import kr.kh.tableup.model.vo.RestaurantVO;

@Service
public class ManagerService {
	
	@Autowired
	ManagerDAO managerDAO;

	 @Value("${spring.path.upload}")
	 String uploadPath;

	@Autowired
  PasswordEncoder passwordEncoder;
	
	public boolean insertManager(RestaurantManagerVO rm) {
		rm.setRm_pw(passwordEncoder.encode(rm.getRm_pw()));
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
		//uploadFileList(restaurant.getRt_num(), fileList);
		
		//매장 정보 등록후 점주에게 매장 정보 부여
		boolean res1 =managerDAO.updateManagerRtNum(manager.getRm_num(),restaurant.getRt_num());
		if(!res1){
			return false;
		}
		
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

	public List<MenuVO> getMenuList(int rt_num) {
		return managerDAO.selectMenuList(rt_num);
	}

	public List<MenuTypeVO> getMenuTypeList() {
		return managerDAO.selectMenuTypeList();
	}

	public boolean makeMenu(MenuVO menu, MultipartFile mn_img) {
		if(menu == null|| mn_img == null|| mn_img.getOriginalFilename().isEmpty()){
			return false;
		}
		boolean res=managerDAO.insertMenu(menu);
		if(!res){
			return false;
		}

		//메뉴 이미지 작업
		String fileName = mn_img.getOriginalFilename();
		String suffix = getSuffix(fileName);
		String newFileName = menu.getMn_num() + suffix;
		String menuImage;
		try{
			menuImage = UploadFileUtils.uploadFile(uploadPath, newFileName, mn_img.getBytes(),"menu");
			menu.setMn_img(menuImage);
			managerDAO.updateMenu(menu);

		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private String getSuffix(String fileName) {
		int index = fileName.lastIndexOf(".");
		return index < 0 ? null : fileName.substring(index);
	}

	public MenuVO getMenu(int mn_num) {
		return managerDAO.selectMenu(mn_num);
	}

	public MenuTypeVO getMenuType(int mn_mt_num) {
		return managerDAO.selectMenuType(mn_mt_num);
	}

	public boolean updateMenu(MenuVO menu, MultipartFile mn_img2) {
		if(menu == null){
			return false;
		}
		//메뉴 이미지 작업
		try{
			String fileName = mn_img2.getOriginalFilename();
			if(mn_img2 != null && fileName.length() !=0){
				String suffix = getSuffix(fileName);
				String newFileName = menu.getMn_num() + suffix;
				String menuImage;
				menuImage = UploadFileUtils.uploadFile(uploadPath, newFileName, mn_img2.getBytes(),"menu");
				menu.setMn_img(menuImage);
			}
			return managerDAO.updateMenu(menu);

		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteMenu(int mn_num) {
		return managerDAO.deleteMenu(mn_num);
	}

	public RestaurantDetailVO getResDetail(int rt_num) {
		return managerDAO.selectResDetail(rt_num);
	}

	public boolean insertResDetail(RestaurantDetailVO resdetail) {
		return managerDAO.insertResDetail(resdetail);
	}

	public boolean updateDetail(RestaurantDetailVO resdetail) {
		return managerDAO.updateDetail(resdetail);
	}

	public List<ResCouponVO> getCouponList(int rt_num) {
		return managerDAO.selectCouponList(rt_num);
	}

	public List<ResNewsVO> getNewsList(int rt_num) {
			return managerDAO.selectNewsList(rt_num);
	}

	public List<BusinessHourVO> getResTimeList(int rt_num) {
		return managerDAO.selectResTimeList(rt_num);
	}

	public boolean makeResTiem(BusinessHourVO restime) {
		if(restime==null || restime.getBh_start() == null|| restime.getBh_end()==null){
			return false;
		}
		boolean res =managerDAO.insertResTime(restime);
		if(!res){
			return false;
		}
		return true;
	}

	public boolean remakeResTime(BusinessHourVO restime) {
		if(restime==null || restime.getBh_start() == null|| restime.getBh_end()==null){
			return false;
		}
		boolean res =managerDAO.updateResTime(restime);
		if(!res){
			return false;
		}
		return true;
	}

}
