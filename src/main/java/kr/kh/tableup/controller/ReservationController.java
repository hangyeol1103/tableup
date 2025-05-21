package kr.kh.tableup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.kh.tableup.service.ManagerService;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
	
	@Autowired
	ManagerService managerService;

	

}
