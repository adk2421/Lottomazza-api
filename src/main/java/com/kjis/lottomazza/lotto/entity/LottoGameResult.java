package com.kjis.lottomazza.lotto.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 로또 당첨 정보를 담는 Entity
 * 
 * @author 김지성
 * created on 2025/03/26
 * 
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lotto_game_result")
public class LottoGameResult {

    @Id
    private String id;                  // MongoDB ObjectId
    private String drwNoDate;           // 추첨일자
    private int drwNo;                  // 회차
    private int drwtNo1;                // 첫 번째 당첨번호
    private int drwtNo2;                // 두 번째 당첨번호
    private int drwtNo3;                // 세 번째 당첨번호
    private int drwtNo4;                // 네 번째 당첨번호
    private int drwtNo5;                // 다섯 번째 당첨번호
    private int drwtNo6;                // 여섯 번째 당첨번호
    private int bnusNo;                 // 보너스 당첨번호
    private String totSellamnt;         // 총 판매금액
    private String firstAccumamnt;      // 1등 총 당첨금액
    private String firstWinamnt;        // 1등 개별 당첨금액
    private int firstPrzwnerCo;         // 1등 당첨 수
    private String returnValue;         // 응답상태

    public void setId(String id) {
        this.id = id;
    }

}