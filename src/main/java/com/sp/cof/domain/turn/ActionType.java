package com.sp.cof.domain.turn;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionType {
    ATTACk("다음 턴 진행"),
    DISCARD("카드 버리기");

    private final String desc;
}
