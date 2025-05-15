package kr.kh.tableup.model.util;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import kr.kh.tableup.model.vo.RestaurantManagerVO;
import lombok.Data;

@Data
public class CustomManager extends User {
	
	private RestaurantManagerVO manager;
	
	public CustomManager(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	public CustomManager(RestaurantManagerVO vo) {
    super(
			vo.getRm_id().toString(),
			vo.getRm_pw(), 
			Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))
    );
    this.manager = vo;
	}
}
