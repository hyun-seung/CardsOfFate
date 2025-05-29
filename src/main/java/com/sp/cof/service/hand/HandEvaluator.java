package com.sp.cof.service.hand;

import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.card.HandRank;
import com.sp.cof.domain.game.HandEvaluationResult;
import com.sp.cof.service.hand.patternchecker.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class HandEvaluator {

    private static List<HandPatternChecker> checkers;

    public HandEvaluator() {
        checkers = List.of(
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

    public static HandEvaluationResult evulate(List<Card> hand) {
        for (HandPatternChecker checker : checkers) {
            if (checker.matches(hand)) {
                return new HandEvaluationResult(checker.getRank(), hand);
            }
        }

        return new HandEvaluationResult(HandRank.HIGH_CARD, hand);
    }
}
