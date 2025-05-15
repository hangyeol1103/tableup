package kr.kh.tableup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.kh.tableup.model.vo.AdminVO;
import kr.kh.tableup.model.vo.FacilityVO;

public interface AdminDAO {

	boolean insertAdmin(AdminVO admin);

	//로그인
	AdminVO selectUser(String username);

	//추가
	boolean insertRegion(@Param("name") String name);
	boolean insertDetailRegion(@Param("name") String name);
	boolean insertTag(@Param("name") String name);
	boolean insertTagType(@Param("name") String name);
	boolean insertFacility(FacilityVO facility);


	
	//출력
	List<String> selectRegionList();
	List<String> selectDetailRegionList();
	List<String> selectTagList();
	List<String> selectTagTypeList();
	List<FacilityVO> selectFacilityList();


}
