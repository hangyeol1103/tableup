package kr.kh.tableup.dao;

import kr.kh.tableup.model.vo.UserVO;

public interface UserDAO {

  UserVO selectUser(String id);

  boolean insertUser(UserVO user);

	int updateUserInfo(UserVO user);

	UserVO selectUserByPhone(String phone);

	UserVO selectUserByEmail(String email);
  
}
