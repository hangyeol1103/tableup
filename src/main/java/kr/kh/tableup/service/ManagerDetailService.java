package kr.kh.tableup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.ManagerDAO;
import kr.kh.tableup.model.util.CustomManager;
import kr.kh.tableup.model.vo.RestaurantManagerVO;

@Service
public class ManagerDetailService implements UserDetailsService{

	@Autowired
	ManagerDAO managerDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("로그인 시도: " + username);
		RestaurantManagerVO manager = managerDAO.selectManager(username);
		if (manager == null) {
            throw new UsernameNotFoundException("존재하지 않는 매니저입니다: " + username);
        }
				System.out.println("불러온 비밀번호: " + manager.getRm_pw());
				System.out.println("UserDetails.getPassword(): " + manager.getRm_pw());
        return new CustomManager(manager);
    }

}