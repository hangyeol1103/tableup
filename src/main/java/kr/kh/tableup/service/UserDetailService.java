package kr.kh.tableup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.UserDAO;
import kr.kh.tableup.model.util.CustomUser;
import kr.kh.tableup.model.vo.UserVO;

@Service
public class UserDetailService implements UserDetailsService{

	@Autowired
	UserDAO userDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			System.out.println("로그인 시도: " + username); 
			UserVO user = userDAO.selectUser(username);
			if (user == null) throw new UsernameNotFoundException("사용자 없음");
			System.out.println("불러온 비밀번호: " + user.getUs_pw());
			System.out.println("UserDetails.getPassword(): " + user.getUs_pw());

			return new CustomUser(user); 
	}


}