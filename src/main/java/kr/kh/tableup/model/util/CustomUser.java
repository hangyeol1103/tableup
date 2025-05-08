package kr.kh.tableup.model.util;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import kr.kh.tableup.model.vo.UserVO;
import lombok.Data;

@Data
public class CustomUser extends User {
	
	private UserVO user;
	
	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	public CustomUser(UserVO vo) {
    super(
			vo.getUs_id().toString(),
			vo.getUs_pw(), 
			Arrays.asList(new SimpleGrantedAuthority(vo.getUs_authority()))
    );
    this.user = vo;
}
}
