package kr.kh.tableup.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.UserDAO;
import kr.kh.tableup.model.vo.UserVO;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    // @Autowired
    // private UserService userService; // 사용자 정보 저장 서비스

    @Autowired
    private UserDAO userDAO;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = new DefaultOAuth2UserService().loadUser(userRequest);

        // 1. 사용자 정보 추출
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google
        Map<String, Object> attributes = oauthUser.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String sub = (String) attributes.get("sub"); // google 고유 id

        // 2. 사용자 존재 여부 확인 후 없으면 자동 가입
        UserVO user = userDAO.selectUserByEmail(email);
        if (user == null) {
            user = new UserVO();
            user.setUs_id("g_" + sub); // 중복 방지
            user.setUs_email(email);
            user.setUs_name(name);
            user.setUs_nickname(name);
            user.setUs_phone("000-0000-0000");
            user.setUs_sociallogin(true);
            user.setUs_pw(createCode(10));
            userDAO.insertUser(user);
            userDAO.insertSocial(user.getUs_num(),"sl_google");
        }
        String userNameAttributeName = userRequest
          .getClientRegistration()
          .getProviderDetails()
          .getUserInfoEndpoint()
          .getUserNameAttributeName();

        // 3. OAuth2User로 반환 (권한 부여 포함)
        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
            attributes,
            userNameAttributeName
        );
    }
      
    private String createCode(int size) {
      String pw = "";
      while(pw.length() < size) {
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



}
