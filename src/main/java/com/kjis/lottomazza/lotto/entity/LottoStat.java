package com.kjis.lottomazza.lotto.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 로또 통계 정보를 담는 Entity
 * 
 * @author 김지성
 * created on 2025/03/26
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lotto_stat")
public class LottoStat {
	
	private String id;				// MongoDB ObjectId
	private int drwNoTotCnt;		// 총 추첨회차 수
	private int[] drwtCnt;			// 번호별 당첨횟수
	private double[] drwtPct;		// 번호별 당첨비율

}
