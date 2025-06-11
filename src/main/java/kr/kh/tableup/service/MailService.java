package kr.kh.tableup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.kh.tableup.model.DTO.EmailDTO;

@Service
public class MailService {
	
@Autowired
private JavaMailSender mailSender;

@Autowired
private PasswordEncoder passwordEncoder;

@Value("${my-email}")
private String setfrom;

	public boolean mailSend(String to, String title, String content) {

		// String setfrom = "이메일주소";
   	try{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper
            = new MimeMessageHelper(message, true, "UTF-8");

        messageHelper.setFrom(setfrom);// 보내는사람 생략하거나 하면 정상작동을 안함
        messageHelper.setTo(to);// 받는사람 이메일
        messageHelper.setSubject(title);// 메일제목은 생략이 가능하다
        messageHelper.setText(content, true);// 메일 내용

        mailSender.send(message);
        return true;
	 	} catch(MessagingException e){
			e.printStackTrace();
			return false;
		
    } catch(Exception e){
        e.printStackTrace();
        return false;
    }
	}

	public String sendEmail(String us_email) {
		if(us_email == null ||us_email.length() < 1) return null;
		
		try {
			//코드 생성
			String code = createCode(6, false);
			// boolean res = mailSend(us_email, "이메일 인증." , "인증 번호는 <b>" + code + "</b> 입니다. 유출되지 않도록 해 주세요.");
			
			if(!mailSend(us_email, "이메일 인증." , "인증 번호는 <b>" + code + "</b> 입니다. 유출되지 않도록 해 주세요.")) return null; //이메일이 잘못됐거나 받는사람이 없는경우 실패
			
		
			return passwordEncoder.encode(code);
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}	

	private String createCode(int size, boolean isPw) {
		if(size<8 && isPw) return null;		//비번 정규표현식보다는 커야함
		String pw = "";
		while(pw.length() < size) {
			//랜덤 정수 생성(0~61)
			
			int r = (int)(Math.random()*(62)); //int r = (int)(Math.random()*(61 - 0 + 1) + 0);
			
			//0~9면 문자 0~9로 맵핑 후 이어붙임
			if(r < 10) pw += r;
			
			//10~35면 a~z로 맵핑 후 이어붙임
			else if (r < 36) pw += (char)(r - 10 + 'a');	//a부터 z까지의 문자
			//36~61이면 A~Z로 맵핑 후 이어붙임
			else pw += (char)(r - 36 + 'A');	//A부터 Z까지의 문자
		}
		return pw;
	}

	public String checkEmail(String ev_key, String code) {
		if(ev_key.isEmpty()||code.isEmpty()||!passwordEncoder.matches(code, ev_key)) return null;

		return "true";
	}
	
}
