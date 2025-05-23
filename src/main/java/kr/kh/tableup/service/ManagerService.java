package kr.kh.tableup.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.kh.tableup.dao.ManagerDAO;
import kr.kh.tableup.model.util.UploadFileUtils;
import kr.kh.tableup.model.vo.BusinessDateVO;
import kr.kh.tableup.model.vo.BusinessHourTemplateVO;
import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.FacilityVO;
import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.MenuTypeVO;
import kr.kh.tableup.model.vo.MenuVO;
import kr.kh.tableup.model.vo.RegionVO;
import kr.kh.tableup.model.vo.ResCouponVO;
import kr.kh.tableup.model.vo.ResNewsVO;
import kr.kh.tableup.model.vo.RestaurantDetailVO;
import kr.kh.tableup.model.vo.RestaurantFacilityVO;
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
		if(rm == null){
			return false;
		}
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
		uploadFileList(restaurant.getRt_num(), fileList);
		
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
			//파일 업로드
			String uploadfilename=UploadFileUtils.uploadFile(uploadPath, fi_ori_name, file.getBytes());
			
			FileVO fileVO =new FileVO(uploadfilename, fi_ori_name , "RESTAURANTDETAIL", String.valueOf(rt_num), "내부", rt_num);
			managerDAO.insertFile(fileVO);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//매장 정보 변경
	public boolean updateRestaurant(RestaurantVO restaurant, RestaurantManagerVO manager, MultipartFile[] fileList) {
		System.out.println("수정할 매장 정보 :"+restaurant);
		System.out.println("매장 매니저 :"+manager);
		
		if(restaurant == null || manager == null){
			System.out.println("수정 실패!");
			return false;
		}
		managerDAO.deletefile(restaurant.getRt_num());
		//매장 이미지 작업
		uploadFileList(restaurant.getRt_num(), fileList);
		
		//매장 정보 변경작업
		boolean res =managerDAO.updateRestaurant(restaurant);
		if(!res){
			return false;
		}
		
		return true;
	}

	//음식분류(대분류) 리스트
	public List<FoodCategoryVO> getFoodCategory() {
		return managerDAO.selectFoodCategoryList();
	}

	//지역(대분류) 리스트
	public List<RegionVO> getRegion() {
	return managerDAO.selectRegionList();
	}

	//지역(소분류) 리스트
	public List<DetailRegionVO> getDetailRegion() {
		return managerDAO.selectDetailRegionList();
	}

	//음식(소분류) 리스트
	public List<DetailFoodCategoryVO> getDetailFood() {
	return managerDAO.selectDetailFoodList();
	}


	public List<DetailFoodCategoryVO> getDetailByFcNum(int fc_num) {
		return managerDAO.selectDetailByFcNum(fc_num);
	}

	public List<DetailRegionVO> getDetailByRegNum(int reg_num) {
		return managerDAO.selectDetailByRegNum(reg_num);
	}

	//메뉴 리스트
	public List<MenuVO> getMenuList(int rt_num) {
		return managerDAO.selectMenuList(rt_num);
	}

	//메뉴 분류 리스트
	public List<MenuTypeVO> getMenuTypeList() {
		return managerDAO.selectMenuTypeList();
	}

	//메뉴 등록
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
		
		System.out.println("-----------------");
		System.out.println("fileName : " + fileName);
		System.out.println(" suffix : " + suffix);
		System.out.println("newFileName : " + newFileName);
		System.out.println("-----------------");
		try{
			menuImage = UploadFileUtils.uploadFile(uploadPath, newFileName, mn_img.getBytes(),"menu");
			
			System.out.println("-----------------");
			System.out.println("menuImage : " + menuImage);
			System.out.println("-----------------");
			
			menu.setMn_img(menuImage);
			System.out.println("menu : " + menu);
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

	//헤당 메뉴 정보 불러오기
	public MenuVO getMenu(int mn_num) {
		return managerDAO.selectMenu(mn_num);
	}

	//헤당 메뉴분류 정보 불러오기
	public MenuTypeVO getMenuType(int mn_mt_num) {
		return managerDAO.selectMenuType(mn_mt_num);
	}

	//메뉴 정보 변경
	public boolean updateMenu(MenuVO menu, MultipartFile mn_img2) {
		System.out.println(menu);
		if(menu == null){
			return false;
		}
		//메뉴 이미지 작업
		try{
			String fileName = mn_img2.getOriginalFilename();
			System.out.println(fileName);
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
	
	//메뉴 정보 삭제
	public boolean deleteMenu(int mn_num) {
		return managerDAO.deleteMenu(mn_num);
	}

	//상세 정보 가져오기
	public RestaurantVO getResDetail(int rt_num) {
		return managerDAO.selectResDetail(rt_num);
	}

	//상세 정보 등록하기
	public boolean insertResDetail(RestaurantVO resdetail) {
		return managerDAO.insertResDetail(resdetail);
	}

	//상세 정보 수정하기
	public boolean updateDetail(RestaurantVO resdetail) {
		return managerDAO.updateDetail(resdetail);
	}

	//쿠폰 리스트
	public List<ResCouponVO> getCouponList(int rt_num) {
		return managerDAO.selectCouponList(rt_num);
	}

	//쿠폰 등록
	public boolean makeCoupon(ResCouponVO coupon) {
		if(coupon == null){
			return false;
		}
		boolean res=managerDAO.insertCoupon(coupon);
		if(!res){
			return false;
		}
		return true;
	}

	//쿠폰 상세 정보
	public ResCouponVO getCoupon(int rec_num) {
		return managerDAO.selectCoupon(rec_num);
	}

	//쿠폰 정보 변경
	public boolean updateCoupon(ResCouponVO coupon) {
		System.out.println(coupon);
		if(coupon == null){
			return false;
		}
		boolean res = managerDAO.updateCoupon(coupon);
		if(!res){
			return false;
		}
		return true;
	}
	
	//쿠폰 정보 삭제
	public boolean deleteCoupon(int rec_num) {
		return managerDAO.deleteCoupon(rec_num);
	}

	//소식 리스트
	public List<ResNewsVO> getNewsList(int rt_num) {
			return managerDAO.selectNewsList(rt_num);
	}

	//예약 가능 시간 출력
	public List<BusinessHourVO> getResTimeList(int rt_num) {
		return managerDAO.selectResTimeList(rt_num);
	}

	//예약 가능 시간 등록
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

	//예약 가능시간 가져오기
	public BusinessHourVO getBusinessHour(int bh_num) {
			return managerDAO.selectBusinessHour(bh_num);
		}

	//예약 가능시간 수정하기
	public boolean remakeResTime(BusinessHourVO restime) {
		System.out.println(restime);
		if(restime==null || restime.getBh_start() == null|| restime.getBh_end()==null){
			System.out.println("수정 실패");
			return false;
		}
		boolean res =managerDAO.updateResTime(restime);
		if(!res){
				System.out.println("수정 실패");
			return false;
		}
		System.out.println("수정 성공");
		return true;
	}

	//예약 가능시간 삭제하기
	public boolean deleteResTime(int bh_num) {
		return managerDAO.deleteResTime(bh_num);
	}

	//영업일자 리스트 출력
	public List<BusinessDateVO> getOperTimeList(int rt_num) {
		return managerDAO.selectOperTimeList(rt_num);
	}

	//영업일자 등록
	public boolean makeOperTime(BusinessDateVO opertime) {
		if(opertime==null){
			return false;
		}
		boolean res =managerDAO.insertOperTime(opertime);
		if(!res){
			System.out.println("등록 실패");
			return false;
		}
		System.out.println("등록 성공");
		return true;
	}

	public BusinessDateVO getBusinessDate(int bd_num) {
		return managerDAO.selectBuisnessDate(bd_num);
	}

	//영업일자 변경
	public boolean remakeOperTime(BusinessDateVO opertime) {
	if(opertime==null){
			return false;
		}
		boolean res =managerDAO.updateOperTime(opertime);
		if(!res){
			System.out.println("수정 실패");
			return false;
		}
		System.out.println("수정 성공");
		return true;
	}

	//영업일자 삭제
	public boolean deleteOperTime(int bd_num) {
		return managerDAO.deleteOperTime(bd_num);
	}

	//소식/공지사항 등록
	public boolean makeNews(ResNewsVO news) {
		if(news == null || news.getRn_content().isEmpty()){
			return false;
		}
		boolean res = managerDAO.insertNews(news);
		if(!res){
			return false;
		}
		return true;
	}
	//공지사항 정보 출력
	public ResNewsVO getNews(int rn_num) {
		return managerDAO.selectResNews(rn_num);
	}

	//공지사항 변경 
	public boolean updateNews(ResNewsVO news) {
		if(news == null || news.getRn_content().isEmpty()){
			return false;
		}
		boolean res = managerDAO.updateNews(news);
		if(!res){
			return false;
		}
		return true;
	}

	public boolean deleteNews(int rn_num) {
		return managerDAO.deleteNews(rn_num);
	}

	public RestaurantVO getRestaurantByNum(int rt_num) {
		return managerDAO.selectRestaurant(rt_num);
	}

	public List<BusinessHourTemplateVO> getTemplateList(int rt_num) {
		return managerDAO.selectTemplateList(rt_num);
	}

	//매장 상세정보
	public List<FacilityVO> getFacilityList() {
		return managerDAO.selectFacilityList();
	}

	public List<RestaurantFacilityVO> getResFacilityList(int rt_num) {
		return managerDAO.selectResFacilityList(rt_num);
	}

	public boolean makeResFacility(RestaurantFacilityVO resfacility) {
		if(resfacility == null || resfacility.getRf_fa_num() == 0){
			return false;
		}
		List<RestaurantFacilityVO> dbResFac = managerDAO.selectResFacilityList(resfacility.getRf_rt_num());

		for(RestaurantFacilityVO dbres : dbResFac){
			if(dbres.getRf_fa_num() == resfacility.getRf_fa_num()){
				System.out.println("이미 등록한 편의시설입니다.");
				return false;
			}
		}

		boolean res =  managerDAO.insertResFacility(resfacility);
		if (!res) {
			return false;
		}
		return true;
	}

	public boolean remakeResFacility(RestaurantFacilityVO resfacility) {
		System.out.println("----------------");
		System.out.println(resfacility);
		if(resfacility == null || resfacility.getRf_fa_num() == 0){
			return false;
		}
		List<RestaurantFacilityVO> dbResFac = managerDAO.selectResFacilityList(resfacility.getRf_rt_num());

		for(RestaurantFacilityVO dbres : dbResFac){
			if(dbres.getRf_fa_num() == resfacility.getRf_fa_num()){
				resfacility.setRf_fa_num(0);
			}
		}
		System.out.println(resfacility);

		boolean res =  managerDAO.updateResFacility(resfacility);
		if (!res) {
			return false;
		}
		return true;
	}

	public RestaurantFacilityVO getResFacility(int rf_num) {
		return managerDAO.selectResFacility(rf_num);
	}

	public boolean deleteResFacility(int rf_num) {
		return managerDAO.deleteResFacility(rf_num);
	}

	public List<FileVO> getFileList(int rm_rt_num) {
		return managerDAO.selectFileList(rm_rt_num);
	}

	public void expireOldCoupons() {
		List<ResCouponVO> expired =managerDAO.getExpiredCoupons(LocalDate.now());
		for(ResCouponVO c: expired){
			c.setRec_state(false);
			managerDAO.updateCouponState(c);
		}
	}


	
}
