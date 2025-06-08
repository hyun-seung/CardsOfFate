package com.sp.cof.service;

import com.sp.cof.common.Constant;
import com.sp.cof.domain.Deck;
import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.enemy.EnemyInfo;
import com.sp.cof.domain.enemy.EnemyStatusDto;
import com.sp.cof.domain.game.GameState;
import com.sp.cof.domain.game.GameStatusDto;
import com.sp.cof.domain.game.HandEvaluationResult;
import com.sp.cof.repository.deck.DeckRepository;
import com.sp.cof.repository.game.GameRepository;
import com.sp.cof.service.hand.HandEvaluator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {

    private final HandService handService;
    private final EnemyService enemyService;
    private final GameHistoryService gameHistoryService;

    private final DeckRepository deckRepository;
    private final GameRepository gameRepository;

    public GameStatusDto startGame(String gameId, long seed) {
        List<Card> hand = createDeckAndHand(gameId, seed);
        GameState gameState = initGameState(gameId);
        gameHistoryService.recordInitialHistory(gameId, hand, gameState);
        return buildGameStatus(gameId, gameState, hand);
    }

    public GameStatusDto getGameStatus(String gameId) {
        GameState state = gameRepository.findByGameId(gameId);
        List<Card> previousHand = gameHistoryService.getHandFromLatestHistory(gameId);
        return buildGameStatus(gameId, state, previousHand);
    }

    public GameStatusDto processTurn(String gameId, List<Card> playerCards) {
        GameState state = gameRepository.findByGameId(gameId);

        HandEvaluationResult handEvaluationResult = evulatePattern(playerCards);
        log.info("HandEvaluationResult : " + handEvaluationResult);
        int damage = handEvaluationResult.getScore();

        Deck deck = deckRepository.findByGameId(gameId);
        EnemyInfo enemy = EnemyInfo.ofRound(state.getCurrentRound());

        List<Card> lastHand = gameHistoryService.getHandFromLatestHistory(gameId);

        boolean enemyDefeated = enemyService.applyDamageToEnemy(state, damage);
        List<Card> updatedHand = handService.updateHandAfterPlay(deck, lastHand, playerCards);
        enemyService.processTurnAndEnemyAttack(state, enemy, enemyDefeated);

        if (enemyDefeated) {
            enemyService.handleEnemyDefeat(state);
        }

        saveState(gameId, state, deck);

        gameHistoryService.recordTurnHistory(gameId, state, updatedHand,
                handEvaluationResult.getCombinationName(), damage, Math.max(0, state.getEnemyHp()));
        return buildGameStatus(gameId, state, updatedHand);
    }

    public GameStatusDto discardCards(String gameId, List<Card> discardedCards) {
        GameState gameState = gameRepository.findByGameId(gameId);
        List<Card> currentHand = gameHistoryService.getHandFromLatestHistory(gameId);

        Deck deck = deckRepository.findByGameId(gameId);
        List<Card> updatedHand = handService.updateHandAfterPlay(deck, currentHand, discardedCards);

        gameState.useDiscard();

        saveState(gameId, gameState, deck);
        gameHistoryService.recordTurnHistory(gameId, gameState, updatedHand, "DISCARD", 0, gameState.getEnemyHp());

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

    private HandEvaluationResult evulatePattern(List<Card> playerCards) {
        return HandEvaluator.evulate(playerCards);
    }

    private void saveState(String gameId, GameState state, Deck deck) {
        gameRepository.save(gameId, state);
        deckRepository.save(gameId, deck);
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
}
