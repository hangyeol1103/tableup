package kr.kh.tableup.model.vo;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class UserVO implements UserDetails{
  private Integer us_num;
  private String us_id;
  private String us_pw;
  private String us_name;
  private String us_phone;
  private String us_email;
  private String us_nickname;
  private Boolean us_sociallogin;
  private Date us_created;
  private int us_state;

  @Override
  public String getUsername() {
    return us_id;
  }

  @Override
  public String getPassword() {
    return us_pw;
  }

  @Override
  public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
    return java.util.Collections.emptyList();
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
    return us_state == 0;
  }
}