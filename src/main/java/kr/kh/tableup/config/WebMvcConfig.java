package kr.kh.tableup.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer{
	
	@Value("${spring.path.upload}")
	String uploadPath;

	public void addResourceHandlers(ResourceHandlerRegistry registry){
		registry.addResourceHandler("/file/**").addResourceLocations("file:///"+uploadPath);
		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/");
	}
}
