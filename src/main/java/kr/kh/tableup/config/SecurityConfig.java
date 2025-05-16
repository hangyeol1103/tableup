package kr.kh.tableup.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import kr.kh.tableup.service.AdminDetailService;
import kr.kh.tableup.service.ManagerDetailService;
import kr.kh.tableup.service.UserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

  @Autowired
  private AdminDetailService adminDetailService;

  @Autowired
  private UserDetailService userDetailService;

  @Autowired
  private ManagerDetailService managerDetailService;

  @Value("${security.rememberme.key}")
  private String rememberMeKey;

  @Bean
    @Order(1)
    public SecurityFilterChain managerSecurityFilterChain(HttpSecurity http) throws Exception {
      http
      .securityMatcher("/manager/**")
      .authorizeHttpRequests(auth -> auth
          .requestMatchers("/manager/signup", "/manager/register").permitAll()
          .anyRequest().authenticated()
      )
      .formLogin(form -> form
          .loginPage("/manager/login")
          .loginProcessingUrl("/manager/login")
          .defaultSuccessUrl("/manager/main")
          .permitAll()
      )
      .logout(logout -> logout
          .logoutUrl("/manager/logout")
          .logoutSuccessUrl("/manager/main")
          .permitAll()
      )
        .userDetailsService(managerDetailService);
  
      return http.build();
    }

	@Bean
  @Order(2)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
      http.securityMatcher("/admin/**")
      .csrf(csrf ->csrf.disable())
      .authorizeHttpRequests((requests) -> requests
        .anyRequest().permitAll()  // 그 외 요청은 인증 필요
      )
      .formLogin((form) -> form
        .loginPage("/admin/login")  // 커스텀 로그인 페이지 설정
        .loginProcessingUrl("/admin/login")//로그인 화면에서 로그인을 눌렀을 때 처리할 url을 지정
        .defaultSuccessUrl("/admin")
        .permitAll()                 // 로그인 페이지는 접근 허용
      )
      .userDetailsService(adminDetailService)
      //자동 로그인 처리
      .rememberMe(rm-> rm
        .userDetailsService(adminDetailService)//자동 로그인할 때 사용할 userDetailService를 추가
        .key(rememberMeKey)//키가 변경되면 기존 토큰이 무효처리
        .rememberMeCookieName("LC_ADMIN")//쿠키 이름
        .tokenValiditySeconds(60 * 60 * 24 * 100)//유지 기간 : x일
      )
      .logout((logout) -> logout
        .logoutUrl("/admin/logout")//post방식
        .logoutSuccessUrl("/admin")
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .permitAll());  // 로그아웃도 모두 접근 가능
        return http.build();
      }

  @Bean
  @Order(3)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
      http
        .securityMatcher("/user/**", "/","/home")
        .csrf(csrf ->csrf.disable())
        .authorizeHttpRequests((requests) -> requests
        .anyRequest().permitAll()  // 그 외 요청은 인증 필요
      )
     
      .formLogin(form -> 
            form
              .loginPage("/user/login")
              .loginProcessingUrl("/user/loginPost")
              .usernameParameter("us_id")
              .passwordParameter("us_pw")
              .defaultSuccessUrl("/")
              .failureHandler(authenticationFailureHandler())
              //.failureUrl("/user/login?error") // 로그인 실패 시 
              .permitAll()
      )
      .userDetailsService(userDetailService)
      //자동 로그인 처리
      .rememberMe(rm-> rm
        .userDetailsService(userDetailService)//자동 로그인할 때 사용할 userDetailService를 추가
        .key(rememberMeKey)//키가 변경되면 기존 토큰이 무효처리
        .rememberMeCookieName("LC")//쿠키 이름
        .tokenValiditySeconds(60 * 60 * 24 * 100)//유지 기간 : x일
      )
      .logout((logout) -> logout
        .logoutUrl("/user/logout")//post방식
        .logoutSuccessUrl("/")
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .permitAll());  // 로그아웃도 모두 접근 가능
        
      return http
        .userDetailsService(userDetailService)    
        .build();
    }
    

    @Bean   // 로그인 실패 핸들러
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            String errorMessage;

            String username = request.getParameter("us_id");
            request.getSession().setAttribute("loginId", username);
            
            if (exception instanceof BadCredentialsException) {
                errorMessage = "아이디 또는 비밀번호가 틀렸습니다.";
            } else if (exception instanceof LockedException) {
                errorMessage = "잠긴 계정입니다.";
            } else {
                errorMessage = "로그인에 실패하였습니다.";
            }

            request.getSession().setAttribute("loginError", errorMessage);
            response.sendRedirect("/user/login?error");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //매니저 로그인
    


}