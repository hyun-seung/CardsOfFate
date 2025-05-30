package com.sp.cof.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.cof.common.Constant;
import com.sp.cof.domain.Deck;
import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.enemy.EnemyInfo;
import com.sp.cof.domain.enemy.EnemyStatusDto;
import com.sp.cof.domain.game.*;
import com.sp.cof.repository.deck.DeckRepository;
import com.sp.cof.repository.deck.InMemoryDeckRepository;
import com.sp.cof.repository.game.GameRepository;
import com.sp.cof.repository.game.InMemoryGameRepository;
import com.sp.cof.repository.history.HistoryRepository;
import com.sp.cof.repository.history.InMemoryHistoryRepository;
import com.sp.cof.service.hand.HandEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        return buildGameStatus(gameId, gameState, hand);
    }

    public GameStatusDto getGameStatus(String gameId) {
        GameState state = gameRepository.findByGameId(gameId);
        GameStateHistory lastTurn = historyRepository.findLatestByGameId(gameId);
        List<Card> previousHand = fromJson(lastTurn.getHandJson());
        return buildGameStatus(gameId, state, previousHand);
    }

    public GameStatusDto processTurn(String gameId, List<Card> playerCards) {
        GameState state = gameRepository.findByGameId(gameId);

        HandEvaluationResult handEvaluationResult = evulatePattern(playerCards);
        log.info("HandEvaluationResult : " + handEvaluationResult);
        int damage = handEvaluationResult.getScore();

        Deck deck = deckRepository.findByGameId(gameId);
        EnemyInfo enemy = EnemyInfo.ofRound(state.getCurrentRound());

        GameStateHistory history = historyRepository.findLatestByGameId(gameId);
        List<Card> lastHand = fromJson(history.getHandJson());

        boolean enemyDefeated = applyDamageToEnemy(state, damage);
        List<Card> updatedHand = updateHand(deck, lastHand, playerCards);
        incrementTurnAddApplyEnemyAttack(state, enemy, enemyDefeated);
        if (enemyDefeated) {
            handleEnemyDefeat(state);
        }

        saveState(gameId, state, deck);

        recordTurnHistory(gameId, state, updatedHand, handEvaluationResult.getCombinationName(), damage,
                Math.max(0, state.getEnemyHp()));

        return buildGameStatus(gameId, state, updatedHand);
    }

    public GameStatusDto discardCards(String gameId, List<Card> discardedCards) {
        GameState gameState = gameRepository.findByGameId(gameId);
        GameStateHistory lastHistory = historyRepository.findLatestByGameId(gameId);
        List<Card> currentHand = fromJson(lastHistory.getHandJson());

        Deck deck = deckRepository.findByGameId(gameId);
        List<Card> updatedHand = updateHand(deck, currentHand, discardedCards);

        gameState.useDiscard();

        saveState(gameId, gameState, deck);
        recordTurnHistory(gameId, gameState, updatedHand, "DISCARD", 0, gameState.getEnemyHp());

        EnemyInfo enemy = EnemyInfo.ofRound(gameState.getCurrentRound());

        return buildGameStatus(gameId, gameState, updatedHand);
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
        gameState.setEnemyHp(EnemyInfo.ROUND_1.getHp());
        gameRepository.save(gameId, gameState);
        return gameState;
    }

    private void recordInitalHistory(String gameId, List<Card> hand, GameState gameState) {
        int round = gameState.getCurrentRound();
        EnemyInfo enemy = EnemyInfo.ofRound(round);

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

    private HandEvaluationResult evulatePattern(List<Card> playerCards) {
        return HandEvaluator.evulate(playerCards);
    }

    private boolean applyDamageToEnemy(GameState state, int damage) {
        int enemyHp = state.getEnemyHp() - damage;
        state.setEnemyHp(enemyHp);
        log.info("EnemyHp : " + enemyHp);
        return enemyHp <= 0;
    }

    private List<Card> updateHand(Deck deck, List<Card> currentHand, List<Card> usedCards) {
        List<Card> updated = new ArrayList<>(currentHand);
        updated.removeAll(usedCards);
        for (int i = 0; i < usedCards.size(); i++) {
            Card drawn = deck.draw();
            if (Objects.nonNull(drawn)) {
                updated.add(drawn);
            }
        }
        return updated;
    }

    private void incrementTurnAddApplyEnemyAttack(GameState state, EnemyInfo enemy, boolean defeated) {
        state.incremnetTurn();
        if (!defeated && state.getCurrentTurn() % enemy.getAttackTurn() == 0) {
            state.damagePlayer(enemy.getAttackPower());
        }
    }

    private void handleEnemyDefeat(GameState gameState) {
        gameState.nextRound();
        gameState.resetTrun();

        EnemyInfo nextEnemy = EnemyInfo.ofRound(gameState.getCurrentRound());
        gameState.setEnemyHp(nextEnemy.getHp());
    }

    private void saveState(String gameId, GameState state, Deck deck) {
        gameRepository.save(gameId, state);
        deckRepository.save(gameId, deck);
    }

    private void recordTurnHistory(String gameId, GameState gameState, List<Card> playerCards, String pattern,
                                   int damage, int enemyHp) {
        String handJson = toJson(playerCards);

        GameStateHistory history = GameStateHistory.builder()
                .gameId(gameId)
                .round(gameState.getCurrentRound())
                .turn(gameState.getCurrentTurn())
                .playerHp(gameState.getPlayerHp())
                .handJson(handJson)
                .enemyHp(gameState.getEnemyHp())
                .build();

        historyRepository.save(history);
    }

    private GameStatusDto buildGameStatus(String gameId, GameState state, List<Card> hand) {
        EnemyInfo enemy = EnemyInfo.ofRound(state.getCurrentRound());
        return new GameStatusDto(
                gameId,
                state.getCurrentRound(),
                state.getCurrentTurn(),
                state.getPlayerHp(),
                hand,
                EnemyStatusDto.from(enemy, state)
        );
    }

    private List<Card> fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("카드 역직렬화 실패", e);
        }
    }
}
