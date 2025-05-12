package com.sp.cof.repository.history;

import com.sp.cof.domain.game.GameStateHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class InMemoryHistoryRepository implements HistoryRepository {

    private final Map<String, GameStateHistory> storage;

    public InMemoryHistoryRepository() {
        this.storage = new ConcurrentHashMap<>();
    }

    @Override
    public void save(GameStateHistory history) {
        storage.put(history.getGameId(), history);
        print();
    }

    @Override
    public GameStateHistory findByGameId(String gameId) {
        return storage.get(gameId);
    }

    private void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=== [History Repository] ===").append("\n");
        storage.forEach((gameId, history) -> {
            sb.append("gameId : ").append(gameId).append(" / ");
            sb.append("History : ").append(history.toString());
            sb.append("\n");
        });
        log.info(sb.toString());
    }
}
