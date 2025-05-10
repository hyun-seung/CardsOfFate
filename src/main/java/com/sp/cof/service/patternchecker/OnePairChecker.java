package com.sp.cof.service.patternchecker;

import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.card.HandRank;
import com.sp.cof.service.HandPatternChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OnePairChecker implements HandPatternChecker {

    @Override
    public boolean matches(List<Card> hand) {
        long pairCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()))
                .values()
                .stream()
                .filter(count -> count == 2L).count();
        return pairCount == 1;
    }

    @Override
    public HandRank getRank() {
        return HandRank.ONE_PAIR;
    }
}
