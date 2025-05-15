package kr.kh.tableup.service;

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

}
