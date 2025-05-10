package com.sp.cof.domain.card;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum CardElement {

    LUNARIA('L', 100, "루나리아", "달의 꽃 - 생명"),
    NOCTIS('N', 200, "녹티스", "밤, 어둠"),
    FORTUNA('F', 300, "포츄나", "운명의 여신"),
    IGNIS('I', 400, "이그니스", "불, 불꽃");

    private final char code;
    private final int value;
    private final String name;
    private final String desc;
}
