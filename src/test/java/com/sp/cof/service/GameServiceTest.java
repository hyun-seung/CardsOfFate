package com.sp.cof.service;

import com.sp.cof.common.Constant;
import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.enemy.EnemyInfo;
import com.sp.cof.domain.game.GameStatusDto;
import com.sp.cof.repository.deck.InMemoryDeckRepository;
import com.sp.cof.repository.game.InMemoryGameRepository;
import com.sp.cof.repository.history.InMemoryHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GameServiceTest {

    private GameService gameService;
    private InMemoryDeckRepository deckRepository;
    private InMemoryGameRepository gameRepository;
    private InMemoryHistoryRepository historyRepository;

    @BeforeEach
    void setUp() {
        deckRepository = new InMemoryDeckRepository();
        gameRepository = new InMemoryGameRepository();
        historyRepository = new InMemoryHistoryRepository();

        gameService = new GameService(deckRepository, gameRepository, historyRepository);
    }

    @Test
    @DisplayName("startGame()은 초기 게임 상태와 핸드를 생성한다")
    void startGame_shouldCreateInitialGameStatus() {
        // given
        String gameId = "game-001";
        long seed = 12345L;

        // when
        GameStatusDto gameStatus = gameService.startGame(gameId, seed);

        // then
        assertThat(gameStatus.gameId()).isEqualTo(gameId);
        assertThat(gameStatus.round()).isEqualTo(1);
        assertThat(gameStatus.turn()).isEqualTo(1);
        assertThat(gameStatus.playerHp()).isEqualTo(Constant.PLAYER_MAX_HP);
        assertThat(gameStatus.hand()).hasSize(Constant.INITIAL_HAND_SIZE);

        assertThat(gameStatus.enemy().attackerPower()).isEqualTo(EnemyInfo.ROUND_1.getAttackPower());
        assertThat(gameStatus.enemy().hp()).isEqualTo(EnemyInfo.ROUND_1.getHp());
        assertThat(gameStatus.enemy().turnsUntilAttack()).isEqualTo(EnemyInfo.ROUND_1.getAttackTurn() - gameStatus.turn());

        List<Card> hand = gameStatus.hand();
        long distinctCount = hand.stream().distinct().count();
        assertThat(distinctCount).isEqualTo(hand.size());
    }
}