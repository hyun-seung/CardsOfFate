package com.sp.cof.service.patternchecker;

import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.card.HandRank;
import com.sp.cof.service.HandPatternChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FullHouseChecker implements HandPatternChecker {

    @Override
    public boolean matches(List<Card> hand) {
        Collection<Long> counts = hand.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()))
                .values();
        return counts.contains(3L) && counts.contains(2L);
    }

    @Override
    public HandRank getRank() {
        return HandRank.FULL_HOUSE;
    }
}
