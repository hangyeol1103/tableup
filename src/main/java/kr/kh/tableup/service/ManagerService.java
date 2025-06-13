package kr.kh.tableup.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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
import kr.kh.tableup.model.vo.DefaultResTimeVO;
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
	
	public String insertManager(RestaurantManagerVO rm) {
		if(rm == null){
			return "잘못된 접근입니다.";
		}
		List<RestaurantManagerVO> dbManager = managerDAO.selectManagerList();

		for(RestaurantManagerVO db: dbManager){
			// 아이디 중복 체크
			if(db.getRm_id().equals(rm.getRm_id())){
				System.out.println("중복된 아이디 입니다.");
				return "중복된 아이디 입니다.";
			}
			// 사업자 번호 중복 체크
			if(db.getRm_business().equals(rm.getRm_business())){
				System.out.println("중복된 사업자 번호 입니다.");
				return "중복된 사업자 번호 입니다.";
			}
			// 전화 번호 중복 체크
			if(db.getRm_phone().equals(rm.getRm_phone())){
				System.out.println("중복된 전화 번호 입니다.");
				return "중복된 전화 번호 입니다.";
			}
		}

		rm.setRm_pw(passwordEncoder.encode(rm.getRm_pw()));
		boolean res=managerDAO.insertManager(rm);
		return res ? null : "회원가입에 실패했습니다.";
	}

	// 조회할 아이디 가져오기
	public RestaurantManagerVO findManager(String rm_name, String rm_phone, String rm_business) {
		if(rm_name == null || rm_phone ==null || rm_business==null){
			return null;
		}
		return managerDAO.selectfindManager(rm_name, rm_phone, rm_business);
	}

	// 비밀번호 변경할 계정 가져오기
	public RestaurantManagerVO getManager(int rm_num) {
		if(rm_num == 0){
			return null;
		}
		return managerDAO.selectResManager(rm_num);
	}

	// 변경할 계정 비밀번호 재설정
	public boolean updateManagerPW(RestaurantManagerVO manager) {
        if(manager == null){
			return false;
		}
		manager.setRm_pw(passwordEncoder.encode(manager.getRm_pw()));
        return managerDAO.updateManagerPassWord(manager);
    }

	public List<RestaurantManagerVO> getManagerList() {
		return managerDAO.selectManagerList();
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
			
			FileVO fileVO =new FileVO(0,uploadfilename, fi_ori_name , "RESTAURANTDETAIL", /*String.valueOf(rt_num)*/rt_num, "내부", rt_num);
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
		managerDAO.deletefileByForeign(restaurant.getRt_num(), "RESTAURANTDETAIL");
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

		if(mn_img2.isEmpty()){
			MenuVO dbMenu = managerDAO.selectMenu(menu.getMn_num());
			if(dbMenu == null){
				return false;
			}
			menu.setMn_img(dbMenu.getMn_img()); // 기존 이미지 유지
			return managerDAO.updateMenu(menu);
		}else{

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
	public boolean makeResTiem(BusinessHourVO restime, Map<String, BusinessDateVO> operDateMap) {
    System.out.println("받은 시간 : " + restime);
    if (restime == null || restime.getBh_start() == null || restime.getBh_end() == null || restime.getBh_seat_max() < 1) {
        return false;
    }

    // ✅ LocalDate 추출
    LocalDate bhDate = restime.getBh_start().toLocalDate();
    String dateStr = bhDate.toString();
    System.out.println("dateStr : " + dateStr);

    BusinessDateVO day = operDateMap.get(dateStr);
    if (day == null) {
        System.out.println("[" + dateStr + "] 영업일자가 없습니다.");
        return false;
    }

    System.out.println("days: " + day);
    System.out.println("bd_open: " + day.getBd_open());

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ✅ LocalTime 추출
    LocalTime resStart = restime.getBh_start().toLocalTime();
    LocalTime resEnd = restime.getBh_end().toLocalTime();
    LocalTime open = LocalDateTime.parse(day.getBd_open(), formatter).toLocalTime();
    LocalTime close = LocalDateTime.parse(day.getBd_close(), formatter).toLocalTime();

    System.out.println("----------------------");

    // ✅ 브레이크 타임 체크
    if (day.getBd_brstart() != null && day.getBd_brend() != null) {
        LocalTime brStart = LocalDateTime.parse(day.getBd_brstart(), formatter).toLocalTime();
        LocalTime brEnd = LocalDateTime.parse(day.getBd_brend(), formatter).toLocalTime();

        if (resStart.isBefore(brEnd) && resEnd.isAfter(brStart)) {
            System.out.println("브레이크 타임 겹침");
            return false;
        }
    }

    System.out.println("----------------------");

    // ✅ 영업시간 범위 체크
    if (resStart.isBefore(open) || resEnd.isAfter(close)) {
        System.out.println("영업시간 범위 초과");
        return false;
    }

    // ✅ 중복 체크
    BusinessHourVO check = managerDAO.checkResTime(
        restime.getBh_rt_num(), restime.getBh_start(), restime.getBh_end()
    );
    System.out.println("check : " + check);
    if (check != null) {
        System.out.println("중복 예약 시간");
        return false;
    }

    boolean res = managerDAO.insertResTime(restime);
    if (!res) {
        System.out.println("오류 발생");
        return false;
    }

    return true;
}

	// 덮어쓰기 하기 위한 기존 파일 삭제
	public void deleteRestimesByDate(int rtNum, LocalDateTime bh_start, LocalDateTime bh_end) {
		managerDAO.deleteRestimesByDate(rtNum, bh_start, bh_end);
	}

	//무시하기 위한 기존 파일 가져오기
	public boolean existsRestime(int rtNum, LocalDateTime bh_start, String bh_date) {
		 return managerDAO.existsRestime(rtNum, bh_start, bh_date);
	}

	//예약 가능시간 가져오기
	public BusinessHourVO getBusinessHour(int bh_num) {
			return managerDAO.selectBusinessHour(bh_num);
		}

	//예약 가능시간 수정하기
	public boolean remakeResTime(BusinessHourVO restime) {
			System.out.println(restime);
			if (restime == null || restime.getBh_date() == null ||
					restime.getBh_start() == null || restime.getBh_end() == null) {
					System.out.println("수정 실패 - 필수 정보 누락");
					return false;
			}

			String date = restime.getBh_date(); // yyyy-MM-dd
			int rt_num = restime.getBh_rt_num();

			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

			// 기존: 시간 문자열 조합 → 현재: LocalDateTime 직접 구성
			LocalTime startTime = restime.getBh_start().toLocalTime();
			LocalTime endTime = restime.getBh_end().toLocalTime();
			LocalDate bhDate = LocalDate.parse(date);

			LocalDateTime bhStart = LocalDateTime.of(bhDate, startTime);
			LocalDateTime bhEnd = LocalDateTime.of(bhDate, endTime);
			restime.setBh_start(bhStart);
			restime.setBh_end(bhEnd);

			System.out.println("생성된 LocalDateTime: " + bhStart + " ~ " + bhEnd);

			// 영업일자 조회
			BusinessDateVO day = managerDAO.selectOperTimeByDate(rt_num, date);
			if (day == null) {
					System.out.println("수정 실패: 영업일자 정보 없음");
					return false;
			}

			LocalTime open = LocalDateTime.parse(day.getBd_open(), dateTimeFormatter).toLocalTime();
			LocalTime close = LocalDateTime.parse(day.getBd_close(), dateTimeFormatter).toLocalTime();

			LocalTime resStart = bhStart.toLocalTime();
			LocalTime resEnd = bhEnd.toLocalTime();

			// 브레이크 타임 검사
			String brStartStr = day.getBd_brstart();
			String brEndStr = day.getBd_brend();
			if (brStartStr != null && !brStartStr.isBlank() &&
					brEndStr != null && !brEndStr.isBlank()) {
					try {
							LocalTime brStart = LocalDateTime.parse(brStartStr, dateTimeFormatter).toLocalTime();
							LocalTime brEnd = LocalDateTime.parse(brEndStr, dateTimeFormatter).toLocalTime();

							if (resStart.isBefore(brEnd) && resEnd.isAfter(brStart)) {
									System.out.println("브레이크 타임 겹침");
									return false;
							}
					} catch (DateTimeParseException e) {
							System.out.println("브레이크 타임 파싱 실패: " + e.getMessage());
					}
			}

			// 영업시간 범위 검사
			if (resStart.isBefore(open) || resEnd.isAfter(close)) {
					System.out.println("영업시간 초과");
					return false;
			}

			// 중복 검사
			BusinessHourVO check = managerDAO.checkUpdateResTime(rt_num, bhStart, bhEnd, restime.getBh_num());
			if (check != null) {
					System.out.println("중복 예약 시간");
					return false;
			}

			// 최종 업데이트
			boolean result = managerDAO.updateResTime(restime);
			if (!result) {
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

	//특정 요일 예약 가능시간 전체 삭제하기
	public boolean deleteAllRestimes(Integer rt_num, String date) {
		return managerDAO.deleteAllRestimes(rt_num, date);
	}

	//영업일자 리스트 출력
	public List<BusinessDateVO> getOperTimeList(int rt_num) {
		return managerDAO.selectOperTimeList(rt_num);
	}

	// 영업일자 등록
	public boolean makeOperTime(BusinessDateVO oper) {
		if (oper == null) return false;
		System.out.println("영업일자 등록 : " + oper);
		try {
			// 날짜 + 시간 조합 (시간이 null이면 변환하지 않음)
			String baseDate = oper.getBd_date(); // yyyy-MM-dd

			oper.setBd_open_ts(toTimestamp(baseDate, oper.getBd_open()));
			oper.setBd_close_ts(toTimestamp(baseDate, oper.getBd_close()));
			oper.setBd_brstart_ts(toTimestamp(baseDate, oper.getBd_brstart()));
			oper.setBd_brend_ts(toTimestamp(baseDate, oper.getBd_brend()));
			oper.setBd_loam_ts(toTimestamp(baseDate, oper.getBd_loam()));
			oper.setBd_lopm_ts(toTimestamp(baseDate, oper.getBd_lopm()));
		} catch (Exception e) {
			System.out.println("시간 변환 오류: " + e.getMessage());
			return false;
		}

		return managerDAO.insertOperTimeStamp(oper);
		}

	private Timestamp toTimestamp(String date, String time) {
		if (date == null || time == null || time.isBlank()) return null;
		return Timestamp.valueOf(date + " " + time + ":00");
	}


	public BusinessDateVO getBusinessDate(int bd_num) {
			BusinessDateVO oper = managerDAO.selectBuisnessDate(bd_num);

			oper.setBd_open(trimTime(oper.getBd_open()));
			oper.setBd_close(trimTime(oper.getBd_close()));
			oper.setBd_brstart(trimTime(oper.getBd_brstart()));
			oper.setBd_brend(trimTime(oper.getBd_brend()));
			oper.setBd_loam(trimTime(oper.getBd_loam()));
			oper.setBd_lopm(trimTime(oper.getBd_lopm()));

			return oper;
	}

	private String trimTime(String datetime) {
			if (datetime == null || datetime.isBlank()) return null;

			try {
					// "2025-05-07 14:50:00" → "14:50:00"
					return datetime.trim().substring(11);
			} catch (Exception e) {
					return null;
			}
	}

	//영업일자 변경
	public boolean remakeOperTime(BusinessDateVO opertime) {
		if(opertime==null){
			return false;
		}
		// 날짜
		String date = opertime.getBd_date();

		// 날짜 + 시간 합치기
		if (opertime.getBd_open() != null)
			opertime.setBd_open(date + " " + opertime.getBd_open() + ":00");

		if (opertime.getBd_close() != null)
			opertime.setBd_close(date + " " + opertime.getBd_close() + ":00");

		if (opertime.getBd_brstart() != null)
			opertime.setBd_brstart(date + " " + opertime.getBd_brstart() + ":00");

		if (opertime.getBd_brend() != null)
			opertime.setBd_brend(date + " " + opertime.getBd_brend() + ":00");

		if (opertime.getBd_loam() != null)
			opertime.setBd_loam(date + " " + opertime.getBd_loam() + ":00");

		if (opertime.getBd_lopm() != null)
			opertime.setBd_lopm(date + " " + opertime.getBd_lopm() + ":00");
		
		System.out.println("수정된 영업일짜 : " + opertime);

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
		return managerDAO.selectRestaurantByNum(rt_num);
	}

	public List<BusinessHourTemplateVO> getTemplateList(int rt_num) {
		return managerDAO.selectTemplateList(rt_num);
	}

	public int updateManagerInfo(RestaurantManagerVO rm) {
		System.out.println(rm);
		
		if(rm==null){
			return 0;
		}
		//비밀번호 암호화
		rm.setRm_pw(passwordEncoder.encode(rm.getRm_pw()));

		return managerDAO.updateManagerInfo(rm);
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

	public void saveTemplates(List<BusinessHourTemplateVO> list) {
			for (BusinessHourTemplateVO vo : list) {
					int count = managerDAO.existsTemplate(vo);
					if (count > 0) {
							managerDAO.updateTemplate(vo);
					} else {
							managerDAO.insertTemplate(vo);
					}
			}
	}

	// 비밀번호 재설정할 계정 확인하기 위한 
    public RestaurantManagerVO findIdAndEmail(String rm_id, String rm_email) {
        if(rm_id == null || rm_email == null){
			return null;
		}
		return managerDAO.selectFindIdAndEmail(rm_id,rm_email);
    }

		//회원가입시 아이디 중복 체크
		public boolean getRestaurantById(String id) {
			if(id==null){
				return true;
			}
			String res= managerDAO.selectManagerId(id);
			if(res != null){
				return true;
			}
			return false;
		}

		public boolean getRestaurantByBusiness(String business) {
			if(business == null){
				return true;
			}
			String res = managerDAO.selectManagerBusiness(business);
			if(res != null){
				return true;
			}
			return false;
		}

		public RestaurantManagerVO getManagerOner(int rt_num) {
			return managerDAO.selectManagerOner(rt_num);
		}

		public boolean selectManagerByIdAndCpw(String rm_id, String inputPw) {
			if (rm_id == null || inputPw == null) {
					return false;
			}

			RestaurantManagerVO dbManager = managerDAO.selectManager(rm_id);
			if (dbManager == null) return false;

			String dbPw = dbManager.getRm_pw(); // 암호화된 비번

			// 평문 비밀번호(inputPw)와 암호화된 비밀번호(dbPw)를 비교
			boolean result = passwordEncoder.matches(inputPw, dbPw);

			System.out.println("입력 비번: " + inputPw);
			System.out.println("DB 비번: " + dbPw);
			System.out.println("비밀번호 일치 여부: " + result);

			return result;
		}

		public void insertDefaultResTimeList(List<DefaultResTimeVO> drtList, int rtNum) {
			if(drtList == null || drtList.size() ==0){
				System.out.println("영업시간 리스트가 비어 있습니다.");
				return;
			}
			for (DefaultResTimeVO drt : drtList) {
					drt.setDrt_rt_num(rtNum); // 매장번호 설정
					System.out.println("저장할 영업시간 정보: " + drt);
					managerDAO.insertDefaultResTime(drt); // DAO에 insert 메서드 호출
			}
		}

		public List<DefaultResTimeVO> getDefaultTimeList(int rt_num) {
			return managerDAO.selectDefaultTimeList(rt_num);
		}

		public void updateDefaultResTimeList(List<DefaultResTimeVO> list, int rtNum) {
			if(list == null || list.size() ==0){
					System.out.println("영업시간 리스트가 비어 있습니다.");
					return;
				}
				for (DefaultResTimeVO drt : list) {
						drt.setDrt_rt_num(rtNum); // 매장번호 설정
						System.out.println("새로 입력받은 영업시간 정보: " + drt);
						managerDAO.updateDefaultResTime(drt); // DAO에 insert 메서드 호출
				}
		}





}
