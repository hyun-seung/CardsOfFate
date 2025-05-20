package com.sp.cof.repository.history;

import com.sp.cof.domain.game.GameStateHistory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class InMemoryHistoryRepositoryTest {

    private InMemoryHistoryRepository historyRepository;

    @BeforeEach
    void setUp() {
        historyRepository = new InMemoryHistoryRepository();
    }

    @DisplayName("GameStateHistory를 저장하면 gameId별로 누적 저장된다")
    @Test
    void save_shouldAccumulateByGameId() {
        String gameId = "gameId-123";
        GameStateHistory history1 = new GameStateHistory(gameId, 1, 2, 100, "I_1", 100);
        GameStateHistory history2 = new GameStateHistory(gameId, 1, 3, 100, "I_2", 90);

        historyRepository.save(history1);
        historyRepository.save(history2);

        List<GameStateHistory> result = historyRepository.findByGameId(gameId);
        assertThat(result).hasSize(2).containsExactly(history1, history2);
    }

    @DisplayName("존재하지 않는 gameId로 조회 시 빈 리스트를 반환한다")
    @Test
    void findByGameId_withUnknownId_shouldReturnEmptyList() {
        String gameId = "notExist-999";

        List<GameStateHistory> result = historyRepository.findByGameId(gameId);

        assertThat(result).isEmpty();
    }

    @DisplayName("gameId 기준으로 가장 최신 round/turn 이력을 반환한다")
    @Test
    void findLastestByGameId_shouldReturnLatest() {
        String gameId = "gameId-123";
        GameStateHistory history1 = new GameStateHistory(gameId, 1, 2, 100, "I_1", 100);
        GameStateHistory history2 = new GameStateHistory(gameId, 1, 3, 100, "I_2", 90);
        GameStateHistory history3 = new GameStateHistory(gameId, 1, 1, 100, "I_3", 100);

        historyRepository.save(history1);
        historyRepository.save(history2);
        historyRepository.save(history3);

        GameStateHistory latest = historyRepository.findLatestByGameId(gameId);

        assertThat(latest).isEqualTo(history2);
    }
}