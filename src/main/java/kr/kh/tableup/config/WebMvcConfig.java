package kr.kh.tableup.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "kr.kh.tableup")
public class WebMvcConfig implements WebMvcConfigurer{
	@Value("${spring.path.upload}")
	String uploadPath;
	

	public void addResourceHandlers(ResourceHandlerRegistry registry){
		registry.addResourceHandler("/upload/**").addResourceLocations("file:///D:/uploads/");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}
}
