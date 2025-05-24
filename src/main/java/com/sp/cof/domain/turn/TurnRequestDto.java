package com.sp.cof.domain.turn;

import com.sp.cof.domain.card.Card;

import java.util.List;

public record TurnRequestDto (
        String gameId,
        List<Card> playedCards,
        ActionType actionType
) {
}
