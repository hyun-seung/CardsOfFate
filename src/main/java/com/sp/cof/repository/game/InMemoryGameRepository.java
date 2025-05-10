package com.sp.cof.repository.game;

import com.sp.cof.domain.game.GameState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class InMemoryGameRepository implements GameRepository {

    private final Map<String, GameState> storage;

    public InMemoryGameRepository() {
        storage = new ConcurrentHashMap<>();
    }

    @Override
    public void save(String gameId, GameState gameState) {
        storage.put(gameId, gameState);
        print();
    }

    private void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=== [Game Repository] ===").append("\n");
        storage.forEach((gameId, gameState) -> {
            sb.append("gameId : ").append(gameId).append(" / ");
            sb.append("GameState : ").append(gameState.toString());
            sb.append("\n");
        });
        log.info(sb.toString());
    }
}
