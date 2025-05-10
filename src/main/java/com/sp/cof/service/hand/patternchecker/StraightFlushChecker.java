package com.sp.cof.service.hand.patternchecker;

import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.card.HandRank;
import com.sp.cof.service.hand.HandPatternChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StraightFlushChecker implements HandPatternChecker {

    @Override
    public boolean matches(List<Card> hand) {
        return new FlushChecker().matches(hand) && new StraightChecker().matches(hand);
    }

    @Override
    public HandRank getRank() {
        return HandRank.STRAIGHT_FLUSH;
    }
}
