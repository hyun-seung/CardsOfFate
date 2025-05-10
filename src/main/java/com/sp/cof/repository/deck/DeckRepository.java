package com.sp.cof.repository.deck;

import com.sp.cof.domain.Deck;

public interface DeckRepository {
    void save(String gameId, Deck deck);
    Deck findById(String gameId);
}
