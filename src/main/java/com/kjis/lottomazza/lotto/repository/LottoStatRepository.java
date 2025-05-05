package com.kjis.lottomazza.lotto.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kjis.lottomazza.lotto.entity.LottoStat;

/**
 * 로또 통계 데이터 접근 Repository
 * 
 * @author 김지성
 * created on 2025/03/26
 * 
 */
public interface LottoStatRepository extends MongoRepository<LottoStat, String> {

	LottoStat findByDrwNoTotCnt(int drwNoTotCnt);
	
}
