package com.sp.cof.domain.record;

import com.sp.cof.domain.entity.Card;

import java.util.List;

public record GameInfo(String gameId, List<Card> hand) {}