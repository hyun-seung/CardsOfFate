package com.sp.cof.service.hand.patternchecker;

import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.card.HandRank;
import com.sp.cof.service.hand.HandPatternChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FourOfAKindChecker implements HandPatternChecker {

    @Override
    public boolean matches(List<Card> hand) {
        return hand.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()))
                .containsValue(4L);
    }

    @Override
    public HandRank getRank() {
        return HandRank.FOUR_OF_A_KIND;
    }
}
