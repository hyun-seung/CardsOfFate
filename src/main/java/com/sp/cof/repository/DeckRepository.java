package com.sp.cof.repository;

import com.sp.cof.domain.Deck;

public interface DeckRepository {

    void save(String gameId, Deck deck);
    Deck findById(String gameId);
}
