package com.sp.cof.repository.game;

import com.sp.cof.domain.game.GameState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryGameRepositoryTest {

    private InMemoryGameRepository gameRepository;

    @BeforeEach
    void setUp() {
        gameRepository = new InMemoryGameRepository();
    }

    @DisplayName("GameState 저장 후 gameId로 조회하면 저장한 GameState를 반환한다")
    @Test
    void saveAndFindByGameId_shouldReturnSavedGameState() {
        // given
        String gameId = "game-001";
        GameState gameState = new GameState(gameId);
        int enemyHp = 999;
        gameState.setEnemyHp(enemyHp);

        // when
        gameRepository.save(gameId, gameState);
        GameState result = gameRepository.findByGameId(gameId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(gameState);
        assertThat(result.getEnemyHp()).isEqualTo(enemyHp);
    }

    @DisplayName("존재하지 않는 gameId로 조회하면 null로 반환한다")
    @Test
    void findByGameId_withUnknownId_shouldReturnNull() {
        // given
        String unknownGameId = "not-exist";

        // when
        GameState result = gameRepository.findByGameId(unknownGameId);

        // then
        assertThat(result).isNull();
    }
}