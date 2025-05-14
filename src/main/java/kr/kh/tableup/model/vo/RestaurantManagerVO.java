package kr.kh.tableup.model.vo;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class RestaurantManagerVO implements UserDetails{

	int rm_num;
	String rm_id;
	String rm_pw;
	String rm_email;
	String rm_name;
	String rm_phone;
	String rm_business;
	int rm_rt_num;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return rm_pw;
	}

	@Override
	public String getUsername() {
		return rm_id;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
