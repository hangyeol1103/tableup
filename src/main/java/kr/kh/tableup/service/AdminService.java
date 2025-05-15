package kr.kh.tableup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.AdminDAO;
import kr.kh.tableup.model.vo.AdminVO;
import kr.kh.tableup.model.vo.FacilityVO;

@Service
public class AdminService {
	
	@Autowired
	AdminDAO adminDAO;

	public boolean insertAdmin(AdminVO admin) {
		return adminDAO.insertAdmin(admin);
	}

	public boolean insertRegion(String name) {
    return adminDAO.insertRegion(name);
	}

	public boolean insertDetailRegion(String name) {
		return adminDAO.insertDetailRegion(name);
	}

	public boolean insertTag(String name) {
		return adminDAO.insertTag(name);
	}

	public boolean insertTagType(String name) {
		return adminDAO.insertTagType(name);
	}

	public boolean insertFacility(FacilityVO facility) {
    return adminDAO.insertFacility(facility);
	}

	public boolean insertMenuType(String name) {
		return adminDAO.insertMenuType(name);
	}

	public boolean insertFoodCategory(String name) {
		return adminDAO.insertFoodCategory(name);
	}

	public boolean insertDetailFoodCategory(String name) {
		return adminDAO.insertDetailFoodCategory(name);
	}



	public List<String> getRegionList() {
    return adminDAO.selectRegionList();
	}

	public List<String> getDetailRegionList() {
    return adminDAO.selectDetailRegionList();
	}

	public List<String> getTagList() {
		return adminDAO.selectTagList();
	}

	public List<String> getTagTypeList() {
		return adminDAO.selectTagTypeList();
	}

	public List<FacilityVO> getFacilityList() {
    return adminDAO.selectFacilityList();
	}

	public List<String> getMenuTypeList() {
		return adminDAO.selectMenuTypeList();
	}

	public List<String> getFoodCategoryList() {
		return adminDAO.selectFoodCategoryList();
	}

	public List<String> getDetailFoodCategoryList() {
		return adminDAO.selectDetailFoodCategoryList();
	}

}
