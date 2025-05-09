package com.sp.cof.service;

import com.sp.cof.domain.entity.Card;
import com.sp.cof.domain.entity.HandRank;

import java.util.List;

public interface HandPatternChecker {
    boolean matches(List<Card> hand);
    HandRank getRank();
}
