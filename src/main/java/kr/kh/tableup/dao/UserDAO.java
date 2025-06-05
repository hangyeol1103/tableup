package kr.kh.tableup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.kh.tableup.model.util.Criteria;
import kr.kh.tableup.model.vo.DefaultResTimeVO;
import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.FacilityVO;
import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.MenuVO;
import kr.kh.tableup.model.vo.ResNewsVO;
import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.RestaurantFacilityVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.model.vo.ReviewScoreVO;
import kr.kh.tableup.model.vo.ReviewVO;
import kr.kh.tableup.model.vo.ScoreTypeVO;
import kr.kh.tableup.model.vo.TagVO;
import kr.kh.tableup.model.vo.UsFollowVO;
import kr.kh.tableup.model.vo.UserVO;

public interface UserDAO {

	UserVO selectUser(int us_num);

  UserVO selectUserById(String us_id);

  boolean insertUser(UserVO user);

	int updateUserInfo(UserVO user);

	UserVO selectUserByPhone(String phone);

	UserVO selectUserByEmail(String email);
  
	List<ReviewVO> selectReviewByUser(int us_num);

	List<ReservationVO> selectReservationByUser(int us_num);

	List<RestaurantVO> selectFollowedRestaurant(Integer us_num);

	List<ReviewVO> selectFollowedReview(Integer us_num);

	List<ScoreTypeVO> selectScoreTypeList();

	boolean insertReview(ReviewVO review);

	boolean insertReviewScore(ReviewScoreVO reviewScore);

	void insertFile(FileVO fileVO);

	List<DetailRegionVO> selectRegionList();

	List<DetailFoodCategoryVO> selectFoodCategoryList();

	List<RestaurantVO> selectRestaurantList(Criteria cri);
	
	int selectCountRestaurantList(Criteria cri);
	// List<RestaurantVO> selectRestaurantList(Map<String, Object> map);
	// int selectCountRestaurantList(Map<String, Object> map);



	List<ReviewVO> selectReviewList();

	List<FacilityVO> selectFacilityList();

	List<TagVO> selectTagList();

	RestaurantVO selectRestaurantDetail(@Param("rt_num") int rt_num);

	FoodCategoryVO selectFoodCategoryByRestaurant(int rt_num);

	DetailFoodCategoryVO selectDetailFoodCategoryByRestaurant(@Param("rt_num") int rt_num);

	TagVO selectTagByRestaurant(@Param("rt_num") int rt_num);


	List<RestaurantFacilityVO> selectRestaurantFacilityList(@Param("rt_num") int rt_num);

	List<UsFollowVO> selectFollowByUser(int us_num);

	UsFollowVO selectUsFollow(UsFollowVO follow);

	int deleteUsFollow(UsFollowVO follow);

	int insertUsFollow(UsFollowVO follow);

	List<ResNewsVO> selectResNewsList(@Param("rt_num") int rt_num);

	List<FileVO> selectFileList(@Param("rt_num") int rt_num);

	List<MenuVO> selectMenuList(@Param("rt_num") int rt_num);

	List<DefaultResTimeVO> selectDefaultResTimeList(@Param("rt_num") int rt_num);

	UserVO selectUserProfileImage(int us_num);

	void insertUserProfileImage(UserVO user);

	void updateUserProfileImage(UserVO user);



}
