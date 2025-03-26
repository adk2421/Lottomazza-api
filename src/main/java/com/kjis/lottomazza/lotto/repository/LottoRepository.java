package com.kjis.lottomazza.lotto.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kjis.lottomazza.lotto.entity.LottoGameResult;

/**
 * 로또 데이터 접근 Repository
 * 
 * @author 김지성
 * created on 2025/03/26
 * 
 */
public interface LottoRepository extends MongoRepository<LottoGameResult, String> {

	LottoGameResult findLottoGameResultById(String id);
	LottoGameResult findLottoGameResultByDrwNo(int drwNo);

}