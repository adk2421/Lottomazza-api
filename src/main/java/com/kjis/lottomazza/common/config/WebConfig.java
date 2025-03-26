package com.kjis.lottomazza.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * CORS 설정 클래스
 * 
 * @author 김지성
 * created on 2025/03/26
 * 
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	Dotenv dotenv = Dotenv.load();

    @SuppressWarnings("null")
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		String VIEW_LOCAL_BASE_URL = dotenv.get("VIEW_LOCAL_BASE_URL");
		String VIEW_PROD_BASE_URL = dotenv.get("VIEW_PROD_BASE_URL");

		registry.addMapping("/**")
				.allowedOrigins(
					VIEW_LOCAL_BASE_URL,
					VIEW_PROD_BASE_URL)
				.allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowCredentials(true)
				.maxAge(3000);
				
    }
}
