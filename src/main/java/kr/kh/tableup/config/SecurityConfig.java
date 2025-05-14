package kr.kh.tableup.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import kr.kh.tableup.service.UserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

  @Autowired
  private UserDetailService userDetailService;

  @Value("${security.rememberme.key}")
  private String rememberMeKey;

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http.csrf(csrf ->csrf.disable())
        .authorizeHttpRequests((requests) -> requests
        //회원과 관리자가 접근할 수 있는 url(MemberInterceptor 역할)
        //.requestMatchers("/post/insert/*").hasAuthority(UserRole.USER.name())
        //관리자만 접근할 수 있는 url(AdminInterceptor 역할)
        //.requestMatchers("/admin/**").hasAnyAuthority(UserRole.ADMIN.name())
        .anyRequest().permitAll()  // 그 외 요청은 인증 필요
      )
      .formLogin(form -> 
            form
              .loginPage("/user/login")
              .loginProcessingUrl("/loginPost")
              .usernameParameter("us_id")
              .passwordParameter("us_pw")
              .defaultSuccessUrl("/")
              .failureUrl("/login?error") // 로그인 실패 시 redirect
              .permitAll()
      )
      //자동 로그인 처리
      .rememberMe(rm-> rm
        .userDetailsService(userDetailService)//자동 로그인할 때 사용할 userDetailService를 추가
        .key(rememberMeKey)//키가 변경되면 기존 토큰이 무효처리
        .rememberMeCookieName("LC")//쿠키 이름
        .tokenValiditySeconds(60 * 60 * 24 * 100)//유지 기간 : x일
      )
      .logout((logout) -> logout
        .logoutUrl("/logout")//post방식
        .logoutSuccessUrl("/")
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .permitAll());  // 로그아웃도 모두 접근 가능
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}