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
		UserVO user = userDAO.selectUser(username);

		return user == null ? null : new CustomUser(user);
	}

}