package com.sp.cof.domain.game;

import com.sp.cof.domain.card.Card;

import java.util.List;

public record GameInfoDto(String gameId, List<Card> hand) {}