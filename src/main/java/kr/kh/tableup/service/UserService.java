package kr.kh.tableup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.UserDAO;
import kr.kh.tableup.model.vo.UserVO;

@Service
public class UserService {
  
  @Autowired
  UserDAO userDAO;

  public boolean insertUser(UserVO user) {
    return userDAO.insertUser(user);
  }

	public UserVO getUserById(String us_id) {
    return userDAO.selectUser(us_id);

	}

  public boolean updateUserInfo(UserVO user) {
    return userDAO.updateUserInfo(user) > 0;
  }

  // 중복 검사
  
  public boolean isIdDuplicate(String id) {
    return userDAO.selectUser(id) != null;
  }
  
  public boolean isPhoneDuplicate(String phone) {
    return userDAO.selectUserByPhone(phone) != null;
  }
  
  public boolean isEmailDuplicate(String email) {
    return userDAO.selectUserByEmail(email) != null;
  }
}
