package com.kjis.lottomazza.lotto.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kjis.lottomazza.lotto.entity.LottoStat;

public interface LottoStatRepository extends MongoRepository<LottoStat, String> {

	LottoStat findByDrwNoTotCnt(int drwNoTotCnt);
	
}
