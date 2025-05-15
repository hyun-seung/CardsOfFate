package com.sp.cof.repository.history;

import com.sp.cof.domain.game.GameStateHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class InMemoryHistoryRepository implements HistoryRepository {

    private final Map<String, List<GameStateHistory>> storage;

    public InMemoryHistoryRepository() {
        this.storage = new ConcurrentHashMap<>();
    }

    @Override
    public void save(GameStateHistory history) {
        storage.computeIfAbsent(history.getGameId(), s -> new ArrayList<>()).add(history);
        print();
    }

    @Override
    public List<GameStateHistory> findByGameId(String gameId) {
        return storage.getOrDefault(gameId, Collections.emptyList());
    }

    @Override
    public GameStateHistory findLatestByGameId(String gameId) {
        List<GameStateHistory> histories = storage.get(gameId);
        if (ObjectUtils.isEmpty(histories)) {
            return null;
        }

        return histories.stream()
                .max(Comparator.comparingInt(GameStateHistory::getRound)
                        .thenComparingInt(GameStateHistory::getTurn))
                .orElse(null);
    }

    private void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=== [History Repository] ===").append("\n");
        storage.forEach((gameId, histories) -> {
            sb.append("gameId : ").append(gameId).append("\n");
            histories.forEach(h -> sb.append(" -> ").append(h).append("\n"));

        });
        log.info(sb.toString());
    }
}
