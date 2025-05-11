package com.sp.cof.repository.game;

import com.sp.cof.domain.game.GameState;

public interface GameRepository {
    void save(String gameId, GameState gameState);
    GameState findByGameId(String gameId);
}
