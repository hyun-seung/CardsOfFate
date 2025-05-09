package com.sp.cof.service;

import com.sp.cof.domain.entity.Card;
import com.sp.cof.domain.entity.HandRank;
import com.sp.cof.domain.record.HandEvaluationResult;
import com.sp.cof.service.patternchecker.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class HandEvaluator {

    private final List<HandPatternChecker> checkers;

    public HandEvaluator() {
        this.checkers = List.of(
                new RoyalStraightFlushChecker(),
                new StraightFlushChecker(),
                new FourOfAKindChecker(),
                new FullHouseChecker(),
                new FlushChecker(),
                new StraightChecker(),
                new ThreeOfAKindChecker(),
                new TwoPairChecker(),
                new OnePairChecker()
        );
    }

    public HandEvaluationResult evulate(List<Card> hand) {
        for (HandPatternChecker checker : checkers) {
            if (checker.matches(hand)) {
                return new HandEvaluationResult(checker.getRank(), hand);
            }
        }

        return new HandEvaluationResult(HandRank.HIGH_CARD, hand);
    }
}
