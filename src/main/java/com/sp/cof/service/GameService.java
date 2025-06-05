package com.sp.cof.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.cof.common.Constant;
import com.sp.cof.domain.Deck;
import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.enemy.EnemyInfo;
import com.sp.cof.domain.enemy.EnemyStatusDto;
import com.sp.cof.domain.game.GameState;
import com.sp.cof.domain.game.GameStateHistory;
import com.sp.cof.domain.game.GameStatusDto;
import com.sp.cof.domain.game.HandEvaluationResult;
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
import java.util.List;

@Slf4j
@Service
public class GameService {

    private final HandService handService;
    private final EnemyService enemyService;

    private final DeckRepository deckRepository;
    private final GameRepository gameRepository;
    private final HistoryRepository historyRepository;

    public GameService(
            HandService handService,
            EnemyService enemyService,
            InMemoryDeckRepository deckRepository,
            InMemoryGameRepository gameRepository,
            InMemoryHistoryRepository historyRepository
    ) {
        this.handService = handService;
        this.enemyService = enemyService;

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

        boolean enemyDefeated = enemyService.applyDamageToEnemy(state, damage);
        List<Card> updatedHand = handService.updateHandAfterPlay(deck, lastHand, playerCards);
        enemyService.processTurnAndEnemyAttack(state, enemy, enemyDefeated);

        if (enemyDefeated) {
            enemyService.handleEnemyDefeat(state);
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
        List<Card> updatedHand = handService.updateHandAfterPlay(deck, currentHand, discardedCards);

        gameState.useDiscard();

        saveState(gameId, gameState, deck);
        recordTurnHistory(gameId, gameState, updatedHand, "DISCARD", 0, gameState.getEnemyHp());

        EnemyInfo enemy = EnemyInfo.ofRound(gameState.getCurrentRound());

        return buildGameStatus(gameId, gameState, updatedHand);
    }

    public List<Card> getRemainingDeck(String gameId) {
        Deck deck = deckRepository.findByGameId(gameId);
        return deck.getRemainingCards();
    }

    private List<Card> createDeckAndHand(String gameId, long seed) {
        Deck deck = new Deck(seed);
        List<Card> hand = handService.createInitialHand(deck);
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
                Constant.MAX_DISCARD_PER_ROUND - state.getDiscardRemainingThisRound(),
                hand,
                EnemyStatusDto.from(enemy, state)
        );
    }

    private List<Card> fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("카드 역직렬화 실패", e);
        }
    }
}
