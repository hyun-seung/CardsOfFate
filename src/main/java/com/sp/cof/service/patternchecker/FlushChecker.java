package com.sp.cof.service.patternchecker;

import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.card.HandRank;
import com.sp.cof.service.HandPatternChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FlushChecker implements HandPatternChecker {

    @Override
    public boolean matches(List<Card> hand) {
        return hand.stream()
                .map(Card::getElement)
                .distinct()
                .count() == 1;
    }

    @Override
    public HandRank getRank() {
        return HandRank.FLUSH;
    }
}
