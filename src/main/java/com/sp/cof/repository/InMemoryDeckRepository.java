package com.sp.cof.repository;

import com.sp.cof.domain.Deck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class InMemoryDeckRepository implements DeckRepository {

    private final Map<String, Deck> storage;

    public InMemoryDeckRepository() {
        storage = new ConcurrentHashMap<>();
    }

    @Override
    public void save(String gameId, Deck deck) {
        storage.put(gameId, deck);
    }

    @Override
    public Deck findById(String gameId) {
        return storage.get(gameId);
    }
}
