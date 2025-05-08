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
}
