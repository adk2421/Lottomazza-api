package com.kjis.lottomazza.lotto.service;

import java.util.Arrays;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjis.lottomazza.lotto.entity.LottoGameResult;
import com.kjis.lottomazza.lotto.entity.LottoStat;
import com.kjis.lottomazza.lotto.repository.LottoGameResultRepository;
import com.kjis.lottomazza.lotto.repository.LottoStatRepository;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 로또 데이터 접근 Service
 * 
 * @author 김지성
 * created on 2025/03/26
 * 
 */
@Slf4j
@NoArgsConstructor
@Service
public class LottoService {

	private Dotenv dotenv = Dotenv.load();
	
	@Autowired
	private LottoGameResultRepository lottoGameResultRepository;

	@Autowired
	private LottoStatRepository lottoStatRepository;

	@Autowired
	private WebClient webClient;

	/**
	 * 로또 당첨정보 총 개수 조회
	 * @return int
	 */
	public int getLottoTotalCount() {
		return (int) lottoGameResultRepository.count();
	}

	/**
	 * 추첨회차로 로또 당첨정보 조회
	 * @param drwNo
	 * @return LottoGameResult
	 */
	public LottoGameResult findLottoByDrwNo(int drwNo) {
		return lottoGameResultRepository.findLottoGameResultByDrwNo(drwNo);
	}

	/**
	 * 로또 당첨정보 저장
	 * @return LottoGameResult
	 */
	@Transactional(timeout = 10)
	public LottoGameResult saveLotto() {
		int saveDrwNo = (int) lottoGameResultRepository.count() + 1; // 저장할 회차번호
		String URL_SUFFIX = dotenv.get("LOTTO_API_GAME_RESULT_PATH_AND_PARAM"); // 로또 API Path 및 Parameter URI
		String url = URL_SUFFIX + (saveDrwNo); // Parameter에 회차번호 붙이기

		// 로또 API Reqeust
		String response = webClient
				.get()
				.uri(url)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
				.retrieve()
				.bodyToMono(String.class)
				.block();

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// 로또 API response 데이터 파싱
			LottoGameResult lotto = objectMapper.readValue(response, LottoGameResult.class);

			// 값이 있으면 DB에 저장
			if ("success".equals(lotto.getReturnValue())) {
				lotto.setId("game_" + lotto.getDrwNo());
				lottoGameResultRepository.save(lotto);

				log.info("[MongoDB Insert Document] lotto_game_result: " + saveDrwNo + "회차 정보 저장");
			}
			return lotto;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new LottoGameResult();
	}

	/**
	 * 로또 통계정보 저장
	 * @return
	 */
	@Transactional(timeout = 10)
	public LottoStat saveLottoStat() {
		int gameTotCnt = (int) lottoGameResultRepository.count(); // 로또 총 추첨횟수
		LottoStat prevLottoStat = lottoStatRepository.findByDrwNoTotCnt(gameTotCnt - 1); // 이전 로또 통계정보
		LottoStat lottoStat = new LottoStat(); // 로또 통계정보를 담을 엔티티

		// 로또 번호별 총 당첨횟수 저장
		int[] drwtCnt = prevLottoStat.getDrwtCnt().clone(); // 로또 번호별 총 당첨횟수 배열
		LottoGameResult lotto = lottoGameResultRepository.findLottoGameResultByDrwNo(gameTotCnt); // 로또 당첨정보

		drwtCnt[0] += 7;
		drwtCnt[lotto.getDrwtNo1()]++;
		drwtCnt[lotto.getDrwtNo2()]++;
		drwtCnt[lotto.getDrwtNo3()]++;
		drwtCnt[lotto.getDrwtNo4()]++;
		drwtCnt[lotto.getDrwtNo5()]++;
		drwtCnt[lotto.getDrwtNo6()]++;
		drwtCnt[lotto.getBnusNo()]++;
		
		// 로또 번호별 당첨비율 저장
		int drwtTotCnt = drwtCnt[0]; // 저장한 로또 당첨번호 총 개수
		double[] drwtPct = new double[46]; // 로또 번호별 당첨비율 배열
		for (int i = 1; i <= 45; i++) {
			drwtPct[i] = (double) drwtCnt[i] / drwtTotCnt; // 로또 번호별 당첨비율
			drwtPct[0] += drwtPct[i]; // 로또 번호별 당첨비율 총합 값
		}

		// 저장할 로또 통계정보 데이터 세팅
		lottoStat.setId("stat_" + gameTotCnt);
		lottoStat.setDrwNoTotCnt(gameTotCnt);
		lottoStat.setDrwtCnt(drwtCnt);
		lottoStat.setDrwtPct(drwtPct);

		// 로또 통계정보 저장
		lottoStatRepository.save(lottoStat);
		log.info("[MongoDB Insert Document] lotto_stat: " + gameTotCnt + "건 통계정보 저장");

		return lottoStat;
	}

	/**
	 * 로또 번호 생성
	 * @return
	 */
	public int[] generateLottoNum() {
		int[] lottoNum = new int[6]; // 로또 번호를 담을 변수
		int gameTotCnt = (int) lottoGameResultRepository.count(); // 로또 총 추첨횟수
		LottoStat lottoStat = lottoStatRepository.findByDrwNoTotCnt(gameTotCnt); // 로또 통계정보

		double[] drwtPct = lottoStat.getDrwtPct(); // 로또 번호별 당첨비율 배열
		double totalDrwtPct = drwtPct[0]; // 로또 번호별 당첨비율 총합

		// 로또 번호 배열
		int[] numbers = new int[45];
        for (int i = 0; i < 45; i++) {
            numbers[i] = i + 1;
        }

		// 로또 번호 랜덤 생성
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			double rand = random.nextDouble(totalDrwtPct); // 랜덤 값 생성

			double sum = 0; // 가중치 변수
			for (int j = 0; j < numbers.length; j++) {
				sum += drwtPct[j+1];

				// 확률 가중치로 로또 번호 넣기
				if (rand <= sum) {
					int num = numbers[j]; // 로또 번호

					// 로또 번호 배열에 중복되는 값이 있으면 번호 재생성
					if (Arrays.stream(lottoNum).anyMatch(n -> n == num)) {
						i--;
					// 중복되는 값이 없다면 번호 넣기
					} else {
						lottoNum[i] = numbers[j];
					}
					break;
				}
			}
		}
		Arrays.sort(lottoNum); // 번호 오름차순 정렬

		return lottoNum;
	}

}
