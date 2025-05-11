package com.sp.cof.repository.deck;

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
        print();
    }

    @Override
    public Deck findByGameId(String gameId) {
        return storage.get(gameId);
    }

    private void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=== [Deck Repository] ===").append("\n");
        storage.forEach((gameId, deck) -> {
            sb.append("gameId : ").append(gameId).append(" / ");
            sb.append("Deck : ").append(deck.toString());
            sb.append("\n");
        });
        log.info(sb.toString());
    }
}
