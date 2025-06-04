package kr.kh.tableup.model.vo;

import java.util.Date;

import org.springframework.security.access.method.P;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserVO{
  private Integer us_num;

  @NotBlank(message = "아이디는 필수입니다.") 
  @Size(min=4, max=16, message = "아이디는 4~16자 사이여야 합니다.")
  private String us_id;
  
  @NotBlank(message = "비밀번호는 필수입니다.") 
  @Size(min=8, max=20, message = "비밀번호는 8~20자 사이여야 합니다.")
  private String us_pw;

  @NotBlank(message = "비밀번호 확인은 필수입니다.") 
  private String us_pw_check;

  @NotBlank(message = "이름은 필수입니다.")
  @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글만 입력 가능합니다.")
  private String us_name;

  @NotBlank(message = "전화번호는 필수입니다.") 
  @Pattern(regexp = "^(01[016789]-\\d{3,4}-\\d{4}|0\\d{1,2}-\\d{3,4}-\\d{4})$", message="전화번호 형식이 아닙니다.")
  private String us_phone;

  @NotBlank(message = "이메일은 필수입니다.") 
  @Email(message = "이메일 형식이 아닙니다.")
  private String us_email;

  
  private String us_nickname; // 공란일 시 자동으로 생성
  
  private Boolean us_sociallogin;
  
  private Date us_created;
  
  private int us_state;

  // User Profile Image fields
  private int upi_num;
  private int upi_us_num;
  private String upi_file_name;
  private String upi_file_path;
  private Date upi_upload_date;

}