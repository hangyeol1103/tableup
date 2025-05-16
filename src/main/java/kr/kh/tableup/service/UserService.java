package kr.kh.tableup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.UserDAO;
import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.model.vo.ReviewVO;
import kr.kh.tableup.model.vo.UserVO;

@Service
public class UserService {
  
  @Autowired
  UserDAO userDAO;

  @Autowired
    private PasswordEncoder passwordEncoder;

  public boolean insertUser(UserVO user) {
    if(user==null || user.getUs_id() == null || user.getUs_pw() == null) {
      return false;
    }
    return userDAO.insertUser(user);
  }



  public boolean updateUserInfo(UserVO user) {
    if(user == null || user.getUs_id() == null) {
      return false;
    }
    return userDAO.updateUserInfo(user) > 0;
  }

  public boolean isIdDuplicate(String id) {
    if (id == null || id.isBlank()) {
      return false;
    }
    return userDAO.selectUser(id) != null;
  }
  
  public boolean isPhoneDuplicate(String phone) {
    if (phone == null || phone.isBlank()) {
      return false;
    }
    return userDAO.selectUserByPhone(phone) != null;
  }
  
  public boolean isEmailDuplicate(String email) {
    if (email == null || email.isBlank()) {
      return false;
    }
    return userDAO.selectUserByEmail(email) != null;
  }


   /** 회원가입*/
  public boolean registerUser(UserVO user) {
    if (user == null || user.getUs_id() == null || user.getUs_pw() == null) {
        return false;
    }

    user.setUs_pw(passwordEncoder.encode(user.getUs_pw()));
    return userDAO.insertUser(user);
  }

  /** 회원 정보 조회 */
  public UserVO getUserById(String us_id) {
      if (us_id == null || us_id.isBlank()) {
          return null;
      }
      return userDAO.selectUser(us_id);
  }

    /** 회원 정보 수정 */
  public boolean updateUser(UserVO user) {
      if (user == null || user.getUs_id() == null) {
          return false;
      }

      // 비밀번호 입력이 있을 경우만 변경
      if (user.getUs_pw() != null && !user.getUs_pw().isBlank()) {
          user.setUs_pw(passwordEncoder.encode(user.getUs_pw()));
      }

      return userDAO.updateUserInfo(user) > 0;
  }

    /** 중복 검사 (ID, 이메일, 전화번호) */
    public boolean isDuplicate(String type, String value) {
        if (type == null || value == null) {
            return false;
        }

        return switch (type) {
            case "id" -> userDAO.selectUser(value) != null;
            case "phone" -> userDAO.selectUserByPhone(value) != null;
            case "email" -> userDAO.selectUserByEmail(value) != null;
            default -> false;
        };
    }



    public List<ReviewVO> getReviewByUser(int us_num) {
      
        return userDAO.selectReviewByUser(us_num);
    }



		public List<ReservationVO> getReservationByUser(int us_num) {
			
      return userDAO.selectReservationByUser(us_num);
		}



    public List<RestaurantVO> getFollowedRestaurant(int us_num) {
      
      return userDAO.selectFollowedRestaurant(us_num);
    }



		public List<ReviewVO> getFollowedReview(int us_num) {
			
      return userDAO.selectFollowedReview(us_num);
		}
}
