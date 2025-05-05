package com.kjis.lottomazza.lotto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kjis.lottomazza.lotto.entity.LottoGameResult;
import com.kjis.lottomazza.lotto.service.LottoService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 로또 데이터 접근 Controller
 * 
 * @author 김지성
 * created on 2025/03/26
 * 
 */
@Slf4j
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
	public LottoGameResult getLottoGameResult(@RequestParam(name = "drwNo") int drwNo) {
		LottoGameResult lotto = lottoService.findLottoByDrwNo(drwNo);
		return lotto;
	}

	/**
	 * 로또 번호 생성
	 * @return
	 */
	@GetMapping("getGenLottoNum")
	public int[] getGenLottoNum() {
		return lottoService.generateLottoNum();
	}
	
}
