package com.sp.cof.domain.game;

import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.enemy.EnemyStatusDto;

import java.util.List;

public record GameStatusDto(
        String gameId,
        int round,
        int turn,
        int playerHp,
        List<Card> hand,
        EnemyStatusDto enemy) {
}
