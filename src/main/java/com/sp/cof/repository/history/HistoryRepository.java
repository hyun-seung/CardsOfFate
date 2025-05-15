package com.sp.cof.repository.history;

import com.sp.cof.domain.game.GameStateHistory;

import java.util.List;

public interface HistoryRepository {
    void save(GameStateHistory history);
    List<GameStateHistory> findByGameId(String gameId);
    GameStateHistory findLatestByGameId(String gameId);
}
