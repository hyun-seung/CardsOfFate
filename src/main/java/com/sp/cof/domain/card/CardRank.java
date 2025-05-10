package com.sp.cof.domain.card;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum CardRank {

    ONE(1, "1", "1"),
    TWO(2, "2", "2"),
    THREE(3, "3", "3"),
    FOUR(4, "4", "4"),
    FIVE(5, "5", "5"),
    SIX(6, "6", "6"),
    SEVEN(7, "7", "7"),
    EIGHT(8, "8", "8"),
    NINE(9, "9",  "9"),
    TEN(10, "10", "10"),
    THREAD(11, "T", "J"),
    WEAVER(12, "W", "Q"),
    FATE(13, "F", "K");

    private final int value;
    private final String code;
    private final String desc;
}
