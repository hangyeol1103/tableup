package kr.kh.tableup.model.vo;

import java.util.Date;

import lombok.Data;

@Data
public class UserVO {
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
  //private String us_authority;
}