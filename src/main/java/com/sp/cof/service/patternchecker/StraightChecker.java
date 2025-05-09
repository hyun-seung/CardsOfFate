package com.sp.cof.service.patternchecker;

import com.sp.cof.domain.entity.Card;
import com.sp.cof.domain.entity.HandRank;
import com.sp.cof.service.HandPatternChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StraightChecker implements HandPatternChecker {

    @Override
    public boolean matches(List<Card> hand) {
        List<Integer> values = hand.stream()
                .map(c -> c.getRank().getValue())
                .distinct()
                .sorted()
                .toList();

        if (values.size() != 5) {
            return false;
        }

        for (int i = 0; i < 4; i++) {
            if (values.get(i) + 1 != values.get(i + 1)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public HandRank getRank() {
        return HandRank.STRAIGHT;
    }
}
