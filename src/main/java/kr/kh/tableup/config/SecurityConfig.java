package kr.kh.tableup.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import kr.kh.tableup.service.ManagerDetailService;
import kr.kh.tableup.service.UserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private UserDetailService userDetailService;

  @Autowired
  private ManagerDetailService managerDetailService;

  @Value("${security.rememberme.key}")
  private String rememberMeKey;

  // 관리자 보안 설정
  @Bean
  @Order(1)
  public SecurityFilterChain managerSecurityFilterChain(HttpSecurity http) throws Exception {
    http
      .securityMatcher("/manager/**")
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/manager/signup", "/manager/register").permitAll()
        .anyRequest().authenticated()
      )
      .formLogin(form -> form
        .loginPage("/manager/login")
        .loginProcessingUrl("/manager/login")
        .defaultSuccessUrl("/manager/main", true)
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

  // 일반 사용자 보안 설정
  @Bean
  @Order(2)
  public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/user/login", "/user/signup", "/css/**", "/js/**").permitAll()
        .anyRequest().authenticated()
      )
      .formLogin(form -> form
        .loginPage("/user/login")
        .loginProcessingUrl("/user/loginPost")
        .defaultSuccessUrl("/", true)
        .permitAll()
      )
      .rememberMe(rm -> rm
        .userDetailsService(userDetailService)
        .key(rememberMeKey)
        .rememberMeCookieName("LC")
        .tokenValiditySeconds(60 * 60 * 24 * 100)
      )
      .logout(logout -> logout
        .logoutUrl("/user/logoutPost")
        .logoutSuccessUrl("/")
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .permitAll()
      )
      .userDetailsService(userDetailService);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
