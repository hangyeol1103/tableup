package kr.kh.tableup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.ManagerDAO;
import kr.kh.tableup.model.vo.RestaurantManagerVO;

@Service
public class ManagerDetailService implements UserDetailsService{

	@Autowired
	ManagerDAO managerDAO;
	
	@Override
	public UserDetails loadUserByUsername(String rm_username) throws UsernameNotFoundException {
		System.out.println("로그인 시도: " + rm_username);  
		RestaurantManagerVO manager = managerDAO.findById( rm_username);
        if (manager == null)
            throw new UsernameNotFoundException("점주 정보 없음");
        
        // 권한이 없다면 ROLE 없이 UserDetails 구현 가능 (단, hasAuthority는 못 씀)
        return manager; 
		
	}

}