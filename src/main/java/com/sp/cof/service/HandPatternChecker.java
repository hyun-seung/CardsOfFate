package com.sp.cof.service;

import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.card.HandRank;

import java.util.List;

public interface HandPatternChecker {
    boolean matches(List<Card> hand);
    HandRank getRank();
}
