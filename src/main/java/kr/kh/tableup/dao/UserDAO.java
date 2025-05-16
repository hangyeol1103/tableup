package kr.kh.tableup.dao;

import java.util.List;

import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.model.vo.ReviewVO;
import kr.kh.tableup.model.vo.UserVO;

public interface UserDAO {

  UserVO selectUser(String us_id);

  boolean insertUser(UserVO user);

	int updateUserInfo(UserVO user);

	UserVO selectUserByPhone(String phone);

	UserVO selectUserByEmail(String email);
  
	List<ReviewVO> selectReviewByUser(int us_num);

	List<ReservationVO> selectReservationByUser(int us_num);

	List<RestaurantVO> selectFollowedRestaurant(Integer us_num);

	List<ReviewVO> selectFollowedReview(Integer us_num);
}
