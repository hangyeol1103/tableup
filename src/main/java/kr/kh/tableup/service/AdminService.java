package kr.kh.tableup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.AdminDAO;
import kr.kh.tableup.model.vo.AdminVO;

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

	public List<String> getRegionList() {
    return adminDAO.selectRegionList();
	}

	public List<String> getDetailRegionList() {
    return adminDAO.selectDetailRegionList();
	}



}
