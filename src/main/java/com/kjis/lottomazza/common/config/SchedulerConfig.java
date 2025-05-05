package com.kjis.lottomazza.common.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kjis.lottomazza.lotto.service.LottoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Scheduler 설정 클래스
 * 
 * @author 김지성
 * created on 2025/05/25
 * 
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

	private final LottoService lottoService;
	
	@Scheduled(cron = "0 0 7 * * MON") // 매주 월요일 오전 7시에 실행
	public void scheduledTask() {
	    lottoService.saveLotto(); // 로또 당첨정보 저장
		lottoService.saveLottoStat(); // 로또 통계정보 저장
		log.info("Scheduled task executed successfully.");
	}

}
