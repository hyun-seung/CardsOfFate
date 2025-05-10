package com.sp.cof.domain.game;

import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.card.HandRank;

import java.util.List;

public record HandEvaluationResult(HandRank rank, List<Card> hand) {

    public int getScore() {
        return rank().getScore();
    }

    public String getCombinationName() {
        return rank.getKoName();
    }
}
