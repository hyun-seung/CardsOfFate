package com.sp.cof.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum HandRank {

    HIGH_CARD(10, "조용한 운명", "Slient Fate", "조합이 없을 경우 제일 높은 카드로 처리"),
    ONE_PAIR(20, "실의 시작", "Thread's Touch", "같은 숫자가 1쌍 있을 경우"),
    TWO_PAIR(40, "교차 실선", "Crossed Threads", "같은 숫자가 2쌍 있을 경우"),
    THREE_OF_A_KIND(80, "운명의 고리", "Fateful Loop", "같은 숫자가 3개 있을 경우"),
    STRAIGHT(100, "운명 흐름", "Fate Flow", "숫자가 연속으로 5개 연결되는 경우"),
    FLUSH(125, "단일 신의 뜻", "One God's Will", "문양이 5개 같은 경우"),
    FULL_HOUSE(175, "운명 직조", "Woven Fate", "같은 숫자 3개 + 2개인 경우"),
    FOUR_OF_A_KIND(400, "고정된 결말", "Fixed Ending", "같은 숫자가 4개 있을 경우"),
    STRAIGHT_FLUSH(600, "운명의 결계", "Fate Barrier", "같은 문양인 숫자가 연속으로 5개 있을 경우"),
    ROYAL_STRAIGHT_FLUSH(2000, "운명의 각성", "Awakened Fate", "10, J, Q, K, A가 같은 문양일 경우");

    private final int score;
    private final String koName;
    private final String enName;
    private final String desc;

    @Override
    public String toString() {
        return getKoName();
    }
}
