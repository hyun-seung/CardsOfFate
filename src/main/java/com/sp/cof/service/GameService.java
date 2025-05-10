package com.sp.cof.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.cof.common.Constant;
import com.sp.cof.domain.Deck;
import com.sp.cof.domain.enemy.EnemyInfo;
import com.sp.cof.domain.enemy.EnemyStatusDto;
import com.sp.cof.domain.game.GameInfoDto;
import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.game.GameState;
import com.sp.cof.domain.game.GameStateHistory;
import com.sp.cof.domain.game.GameStatusDto;
import com.sp.cof.repository.deck.DeckRepository;
import com.sp.cof.repository.deck.InMemoryDeckRepository;
import com.sp.cof.repository.game.GameRepository;
import com.sp.cof.repository.game.InMemoryGameRepository;
import com.sp.cof.repository.history.HistoryRepository;
import com.sp.cof.repository.history.InMemoryHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class GameService {

    private final DeckRepository deckRepository;
    private final GameRepository gameRepository;
    private final HistoryRepository historyRepository;

    public GameService(
            InMemoryDeckRepository deckRepository,
            InMemoryGameRepository gameRepository,
            InMemoryHistoryRepository historyRepository
    ) {
        this.deckRepository = deckRepository;
        this.gameRepository = gameRepository;
        this.historyRepository = historyRepository;
    }

    public GameStatusDto startGame(String gameId, long seed) {
        List<Card> hand = createDeckAndHand(gameId, seed);
        GameState gameState = initGameState(gameId);
        recordInitalHistory(gameId, hand, gameState);
        return new GameStatusDto(
                gameId,
                gameState.getCurrentRound(),
                gameState.getCurrentTurn(),
                gameState.getPlayerHp(),
                hand,
                EnemyStatusDto.from(EnemyInfo.ROUND_1)
        );
    }

    public GameInfoDto getGameInfo(String gameId) {
        Deck deck = deckRepository.findById(gameId);
        if (Objects.isNull(deck)) {
            throw new IllegalArgumentException("해당 게임 ID의 덱이 존재하지 않습니다. gameId : " + gameId);
        }

        List<Card> hand = deck.getCurrentHand();
        return new GameInfoDto(gameId, hand);
    }

    private List<Card> createDeckAndHand(String gameId, long seed) {
        Deck deck = new Deck(seed);

        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < Constant.INITIAL_HAND_SIZE; i++) {
            hand.add(deck.draw());
        }

        deckRepository.save(gameId, deck);
        return hand;
    }

    private GameState initGameState(String gameId) {
        GameState gameState = new GameState(gameId);
        gameRepository.save(gameId, gameState);
        return gameState;
    }

    private void recordInitalHistory(String gameId, List<Card> hand, GameState gameState) {
        int round = gameState.getCurrentRound();
        EnemyInfo.ofRound(round).orElseThrow(() -> new IllegalStateException("ROUND_" + round + " enemy not found"));

        String handJson = toJson(hand);

        GameStateHistory history = GameStateHistory.builder()
                .gameId(gameId)
                .round(EnemyInfo.ROUND_1.getRound())
                .playerHp(Constant.PLAYER_MAX_HP)
                .handJson(handJson)
                .enemyHp(EnemyInfo.ROUND_1.getHp())
                .build();

        historyRepository.save(history);
    }

    private String toJson(List<Card> hand) {
        try {
            return new ObjectMapper().writeValueAsString(hand);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("hand 직렬화 실패", e);
        }
    }

}
