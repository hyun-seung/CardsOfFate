package com.sp.cof.service.patternchecker;

import com.sp.cof.domain.entity.Card;
import com.sp.cof.domain.entity.HandRank;
import com.sp.cof.service.HandPatternChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RoyalStraightFlushChecker implements HandPatternChecker {

    private static final Set<Integer> ROYAL_VALUES = Set.of(1, 10, 11, 12, 13);

    @Override
    public boolean matches(List<Card> hand) {
        if (!new FlushChecker().matches(hand)) {
            return false;
        }

        Set<Integer> values = hand.stream()
                .map(c -> c.getRank().getValue())
                .collect(Collectors.toSet());

        return values.size() == 5 && values.containsAll(ROYAL_VALUES);
    }

    @Override
    public HandRank getRank() {
        return HandRank.ROYAL_STRAIGHT_FLUSH;
    }
}
