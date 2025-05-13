package kr.kh.tableup.model.util;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import kr.kh.tableup.model.vo.AdminVO;
import lombok.Data;

@Data
public class CustomAdmin extends User {
	
	private AdminVO admin;
	
	public CustomAdmin(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	public CustomAdmin(AdminVO vo) {
    super(
			vo.getAd_id().toString(),
			vo.getAd_pw(), 
			Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))
    );
    this.admin = vo;
	}
}
