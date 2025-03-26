package com.kjis.lottomazza.lotto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjis.lottomazza.lotto.entity.LottoGameResult;
import com.kjis.lottomazza.lotto.repository.LottoRepository;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.NoArgsConstructor;

/**
 * 로또 데이터 접근 Service
 * 
 * @author 김지성
 * created on 2025/03/26
 * 
 */
@NoArgsConstructor
@Service
public class LottoService {
	private Dotenv dotenv = Dotenv.load();
	
	@Autowired
	private LottoRepository lottoRepository;

	@Autowired
	private WebClient webClient;

	public long getLottoTotalCount() {
		return lottoRepository.count();
	}

	public LottoGameResult findLottoByDrwNo(int drwNo) {
		return lottoRepository.findLottoGameResultByDrwNo(drwNo);
	}

	@Transactional(timeout = 10)
	public LottoGameResult saveLotto(int drwNo) {
		String URL_PREFIX = dotenv.get("LOTTO_API_GAME_RESULT_PATH_AND_PARAM");
		String url = URL_PREFIX + drwNo;

		String response = webClient
				.get()
				.uri(url)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
				.retrieve()
				.bodyToMono(String.class)
				.block();

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			LottoGameResult lotto = objectMapper.readValue(response, LottoGameResult.class);

			if ("success".equals(lotto.getReturnValue())) {
				lotto.setId("game_" + lotto.getDrwNo());
				lottoRepository.save(lotto);
			}
			return lotto;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new LottoGameResult();
	}

}
