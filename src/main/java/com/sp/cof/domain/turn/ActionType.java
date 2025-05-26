package com.sp.cof.domain.turn;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "플레이어의 행동 타입")
public enum ActionType {

    @Schema(description = "공격")
    ATTACK("다음 턴 진행"),

    @Schema(description = "카드 버리기")
    DISCARD("카드 버리기");

    private final String desc;
}
