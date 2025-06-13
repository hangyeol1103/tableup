package kr.kh.tableup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.AdminDAO;
import kr.kh.tableup.model.util.CustomAdmin;
import kr.kh.tableup.model.vo.AdminVO;

@Service
public class AdminDetailService implements UserDetailsService{

	@Autowired
	AdminDAO adminDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AdminVO admin = adminDAO.selectUser(username);

		if (admin == null) {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다: " + username);
        }

        return new CustomAdmin(admin);
    }

}