package com.sp.cof.domain.record;

import com.sp.cof.domain.entity.Card;
import com.sp.cof.domain.entity.HandRank;

import java.util.List;

public record HandEvaluationResult(HandRank rank, List<Card> hand) {

    public int getScore() {
        return rank().getScore();
    }

    public String getCombinationName() {
        return rank.getKoName();
    }
}
