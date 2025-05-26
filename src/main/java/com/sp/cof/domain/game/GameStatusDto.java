package com.sp.cof.domain.game;

import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.enemy.EnemyStatusDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "게임 상태 응답")
public record GameStatusDto(

        @Schema(description = "게임 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        String gameId,

        @Schema(description = "현재 라운드", example = "1")
        int round,

        @Schema(description = "현재 턴", example = "1")
        int turn,

        @Schema(description = "플레이어 체력", example = "100")
        int playerHp,

        @Schema(description = "현재 핸드 카드", example = "[\"N_7\", \"L_9\"]")
        List<Card> hand,

        @Schema(description = "적 정보")
        EnemyStatusDto enemy
) {
}
