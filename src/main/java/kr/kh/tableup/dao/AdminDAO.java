package kr.kh.tableup.dao;

import kr.kh.tableup.model.vo.AdminVO;

public interface AdminDAO {

	boolean insertAdmin(AdminVO admin);

	//로그인
	AdminVO selectUser(String username);
  
}
