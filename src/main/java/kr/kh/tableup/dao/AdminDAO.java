package kr.kh.tableup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.kh.tableup.model.vo.AdminVO;

public interface AdminDAO {

	boolean insertAdmin(AdminVO admin);

	//로그인
	AdminVO selectUser(String username);

	//지역 추가
	boolean insertRegion(@Param("name") String name);

	//지역 출력
	List<String> selectRegionList();

	

}
