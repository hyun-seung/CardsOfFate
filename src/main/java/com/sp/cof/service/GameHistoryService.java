package com.sp.cof.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.cof.common.Constant;
import com.sp.cof.common.ErrorCode;
import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.enemy.EnemyInfo;
import com.sp.cof.domain.game.GameState;
import com.sp.cof.domain.game.GameStateHistory;
import com.sp.cof.exception.BusinessException;
import com.sp.cof.repository.history.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameHistoryService {

    private final HistoryRepository historyRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void recordInitialHistory(String gameId, List<Card> hand, GameState gameState) {
        validateHistroyInputs(gameId, hand, gameState);

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

    public void recordTurnHistory(String gameId, GameState gameState, List<Card> playerCards,
                                  String combinationName, int damage, int enemyHp) {
        validateTurnHistoryInputs(gameId, gameState, playerCards, combinationName);

        String handJson = toJson(playerCards);

        GameStateHistory history = GameStateHistory.builder()
                .gameId(gameId)
                .round(gameState.getCurrentRound())
                .turn(gameState.getCurrentTurn())
                .playerHp(gameState.getPlayerHp())
                .handJson(handJson)
                .enemyHp(enemyHp)
                .build();

        historyRepository.save(history);
        log.info("게임 {} 턴 {} 히스토리 기록 완료 - 조합: {}, 데미지: {}",
                gameId, gameState.getCurrentTurn(), combinationName, damage);
    }

    public List<Card> getHandFromLatestHistory(String gameId) {
        validateGameId(gameId);

        GameStateHistory latestHistory = historyRepository.findLatestByGameId(gameId);
        if (latestHistory == null) {
            throw new BusinessException(ErrorCode.HISTORY_NOT_FOUND);
        }

        return fromJson(latestHistory.getHandJson());
    }

    public String toJson(List<Card> cards) {
        if (ObjectUtils.isEmpty(cards)) {
            throw new BusinessException(ErrorCode.INVALID_CARDS_FOR_JSON);
        }

        try {
            String json = objectMapper.writeValueAsString(cards);
            return json;
        } catch (JsonProcessingException e) {
            log.error("카드 직렬화 실패 : {}", e.getMessage());
            throw new BusinessException(ErrorCode.JSON_SERIALIZATION_FAILED);
        }
    }

    public List<Card> fromJson(String json) {
        if (ObjectUtils.isEmpty(json)) {
            throw new BusinessException(ErrorCode.INVALID_JSON_STRING);
        }

        try {
            List<Card> cards = objectMapper.readValue(json, new TypeReference<List<Card>>() {
            });
            return cards;
        } catch (IOException e) {
            log.error("카드 역직렬화 실패 : {}", e.getMessage());
            throw new BusinessException(ErrorCode.JSON_DESERIALIZATION_FAILED);
        }
    }

    private void validateTurnHistoryInputs(String gameId, GameState gameState, List<Card> playerCards, String combinationName) {
        validateGameId(gameId);
        if (Objects.isNull(gameState)) {
            throw new BusinessException(ErrorCode.INVALID_GAME_STATE);
        }
        if (Objects.isNull(playerCards)) {
            throw new BusinessException(ErrorCode.INVALID_PLAYER_CARDS);
        }
        if (ObjectUtils.isEmpty(combinationName)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateHistroyInputs(String gameId, List<Card> hand, GameState gameState) {
        validateGameId(gameId);
        if (ObjectUtils.isEmpty(hand)) {
            throw new BusinessException(ErrorCode.INVALID_HAND_FOR_HISTORY);
        }
        if (Objects.isNull(gameState)) {
            throw new BusinessException(ErrorCode.INVALID_GAME_STATE);
        }
    }

    private void validateGameId(String gameId) {
        if (ObjectUtils.isEmpty(gameId)) {
            throw new BusinessException(ErrorCode.INVALID_GAME_ID);
        }
    }


}
