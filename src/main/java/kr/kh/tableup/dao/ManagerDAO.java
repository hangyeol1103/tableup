package kr.kh.tableup.dao;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

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
import kr.kh.tableup.model.vo.RestaurantDetailVO;
import kr.kh.tableup.model.vo.RestaurantFacilityVO;
import kr.kh.tableup.model.vo.RestaurantManagerVO;
import kr.kh.tableup.model.vo.RestaurantVO;

public interface ManagerDAO {
	
	List<RestaurantManagerVO> selectManagerList();

	boolean insertManager(RestaurantManagerVO rm);
	
	RestaurantManagerVO selectfindManager(String rm_name, String rm_phone, String rm_business);

	RestaurantManagerVO selectResManager(int rm_num);
	
	boolean updateManagerPassWord(RestaurantManagerVO manager);

	RestaurantManagerVO findById(String username);

	RestaurantManagerVO selectManager(String rm_id);

	RestaurantVO selectRestaurant(int rm_rum);

	boolean insertRestaurant(RestaurantVO restaurant);

	List<FoodCategoryVO> selectFoodCategoryList();

	List<RegionVO> selectRegionList();

	List<DetailFoodCategoryVO> selectDetailFoodList();

	List<DetailRegionVO> selectDetailRegionList();

	List<DetailFoodCategoryVO> selectDetailByFcNum(int fc_num);

	List<DetailRegionVO> selectDetailByRegNum(int reg_num);

	boolean updateManagerRtNum(@Param("rm_num")int rm_num,@Param("rt_num") int rt_num);

	List<MenuVO> selectMenuList(int rt_num);

	List<MenuTypeVO> selectMenuTypeList();

	boolean insertMenu(MenuVO menu);

	boolean updateMenu(MenuVO menu);

	MenuVO selectMenu(int mn_num);

	MenuTypeVO selectMenuType(int mn_mt_num);

	boolean deleteMenu(int mn_num);

	RestaurantVO selectResDetail(int rt_num);

	boolean insertResDetail(RestaurantDetailVO resdetail);

	boolean updateDetail(RestaurantDetailVO resdetail);

	List<ResCouponVO> selectCouponList(int rt_num);

	boolean insertCoupon(ResCouponVO coupon);

	List<ResNewsVO> selectNewsList(int rt_num);

	List<BusinessHourVO> selectResTimeList(int rt_num);

	BusinessHourVO checkResTime(int bh_rt_num, LocalDateTime bh_start, LocalDateTime bh_end);

	boolean insertResTime(BusinessHourVO restime);

	BusinessHourVO selectBusinessHour(int bh_num);
	
	BusinessDateVO selectOperTimeByDate(int rt_num, String date);

	BusinessHourVO checkUpdateResTime(int bh_rt_num, LocalDateTime bh_start, LocalDateTime bh_end, int bh_num);

	boolean updateResTime(BusinessHourVO restime);

	boolean deleteResTime(int bh_num);

	List<BusinessDateVO> selectOperTimeList(int rt_num);

	boolean insertOperTime(BusinessDateVO opertime);

	boolean insertOperTimeStamp(BusinessDateVO oper);

	BusinessDateVO selectBuisnessDate(int bd_num);

	boolean insertResDetail(RestaurantVO resdetail);

	boolean updateDetail(RestaurantVO resdetail);

	boolean updateOperTime(BusinessDateVO opertime);

	boolean deleteOperTime(int bd_num);

	ResCouponVO selectCoupon(int rec_num);

	boolean updateCoupon(ResCouponVO coupon);

	boolean deleteCoupon(int rec_num);

	boolean insertNews(ResNewsVO news);

	ResNewsVO selectResNews(int rn_num);

	boolean updateNews(ResNewsVO news);

	boolean deleteNews(int rn_num);

	List<BusinessHourTemplateVO> selectTemplateList(int rt_num);

	int updateManagerInfo(RestaurantManagerVO rm);
	
	List<FacilityVO> selectFacilityList();

	List<RestaurantFacilityVO> selectResFacilityList(int rt_num);

	boolean insertResFacility(RestaurantFacilityVO resfacility);

	boolean updateResFacility(RestaurantFacilityVO resfacility);

	RestaurantFacilityVO selectResFacility(int rf_num);

	boolean deleteResFacility(int rf_num);

	void insertFile(FileVO fileVO);

	List<FileVO> selectFileList(int rm_rt_num);

	void deletefileByForeign(int foreign, String type);

	boolean updateRestaurant(RestaurantVO restaurant);

	List<ResCouponVO> getExpiredCoupons(LocalDate now);

	void updateCouponState(ResCouponVO c);

	RestaurantVO selectRestaurantByNum(int rt_num);

	void deleteTemplate(int bhd_rt_num);

	void insertTemplate(BusinessHourTemplateVO vo);

	int existsTemplate(BusinessHourTemplateVO vo);

	void updateTemplate(BusinessHourTemplateVO vo);

	void deleteRestimesByDate(int rt_num, LocalDateTime bh_start, LocalDateTime bh_end);

	boolean existsRestime(int rtNum, LocalDateTime bh_start, String bh_date);

	boolean deleteAllRestimes(Integer rt_num, String date);

  RestaurantManagerVO selectFindIdAndEmail(String rm_id, String rm_email);

	//아이디 중복 체크
	String selectManagerId(String id);

	//사업자 번호 중복
	String selectManagerBusiness(String business);

	RestaurantManagerVO selectManagerOner(int rt_num);

	void insertDefaultResTime(DefaultResTimeVO drt);

	List<DefaultResTimeVO> selectDefaultTimeList(int rt_num);

	void updateDefaultResTime(DefaultResTimeVO drt);




	
}
