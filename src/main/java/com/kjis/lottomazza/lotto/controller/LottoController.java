package com.kjis.lottomazza.lotto.controller;

import org.springframework.web.bind.annotation.RestController;

import com.kjis.lottomazza.lotto.entity.LottoGameResult;
import com.kjis.lottomazza.lotto.service.LottoService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 로또 데이터 접근 Controller
 * 
 * @author 김지성
 * created on 2025/03/26
 * 
 */
@AllArgsConstructor
@RestController
public class LottoController {

	private LottoService lottoService;

	/**
	 * 회차별 당첨번호 조회
	 * @param drwNo
	 * @return
	 */
	@GetMapping("getLottoGameResult")
	public LottoGameResult getLottoGameResult(@RequestParam int drwNo) {
		System.out.println("데이터 총 개수 : " + lottoService.getLottoTotalCount());

		LottoGameResult lotto = lottoService.findLottoByDrwNo(drwNo);

		// 조회 데이터가 없다면 로또 API를 통해 데이터 저장
		// TODO: 스케줄러로 관리
		if (lotto == null) {
			lotto = lottoService.saveLotto(drwNo);
		}

		return lotto;
	}
	
}
