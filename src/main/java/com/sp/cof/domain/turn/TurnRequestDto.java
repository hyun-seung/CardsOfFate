package com.sp.cof.domain.turn;

import com.sp.cof.domain.card.Card;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "턴 처리 요청 정보")
public record TurnRequestDto (

        @Schema(description = "게임 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        String gameId,

        @Schema(description = "사용한 카드 목록", example = "[\"L_3\", \"N_7\"]")
        List<Card> playedCards,

        @Schema(description = "행동 타입: ATTACK 또는 DISCARD", example = "ATTACK")
        ActionType actionType
) {
}
