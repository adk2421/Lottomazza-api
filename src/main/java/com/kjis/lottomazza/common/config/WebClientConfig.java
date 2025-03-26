package com.kjis.lottomazza.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * WebClient 설정 클래스
 * 
 * @author 김지성
 * created on 2025/03/26
 * 
 */
@Configuration
public class WebClientConfig {
	Dotenv dotenv = Dotenv.load();

	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		String LOTTO_API_BASE_URL = dotenv.get("LOTTO_API_BASE_URL");

		return builder
			.baseUrl(LOTTO_API_BASE_URL)
			.defaultHeaders(httpHeaders -> {
				httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			})
			.build();
	}
	
}