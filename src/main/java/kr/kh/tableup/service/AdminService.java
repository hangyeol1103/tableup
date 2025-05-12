package kr.kh.tableup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.AdminDAO;

@Service
public class AdminService {
	
	@Autowired
	AdminDAO adminDAO;
}
