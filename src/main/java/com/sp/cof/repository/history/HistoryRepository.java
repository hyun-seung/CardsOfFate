package com.sp.cof.repository.history;

import com.sp.cof.domain.game.GameStateHistory;

public interface HistoryRepository {
    void save(GameStateHistory history);
    GameStateHistory findByGameId(String gameId);
}
