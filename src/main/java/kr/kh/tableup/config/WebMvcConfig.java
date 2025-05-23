package kr.kh.tableup.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "kr.kh.tableup") 
public class WebMvcConfig implements WebMvcConfigurer{
	
	@Value("${spring.path.upload}")
	String uploadPath;

	public void addResourceHandlers(ResourceHandlerRegistry registry){
		registry.addResourceHandler("/file/**").addResourceLocations("file:///"+uploadPath);
		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/");
	}

	@Override
	public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:8080") 
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowCredentials(true); 
 	}
	@Override
	public Validator getValidator() {
			return new LocalValidatorFactoryBean();
	}
	/*
	@Override
	public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addInterceptors(registry);
		registry.addInterceptor(new kr.kh.tableup.interceptor.UserInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/user/login", "/user/signup", "/user/register", "/user/logout", "/user/findId", "/user/findPw");
		registry.addInterceptor(new kr.kh.tableup.interceptor.ManagerInterceptor())
			.addPathPatterns("/manager/**")
			.excludePathPatterns("/manager/login", "/manager/signup", "/manager/register");
			}
*/

}
