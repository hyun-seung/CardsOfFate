package com.sp.cof.service;

import com.sp.cof.common.ErrorCode;
import com.sp.cof.domain.enemy.EnemyInfo;
import com.sp.cof.domain.game.GameState;
import com.sp.cof.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class EnemyService {

    public boolean applyDamageToEnemy(GameState gameState, int damage) {
        validateDamageInput(gameState, damage);

        int currentEnemyHp = gameState.getEnemyHp();
        int newEnemyHp = Math.max(0, currentEnemyHp - damage);

        boolean isDefeated = newEnemyHp <= 0;
        if (isDefeated) {
            log.info("적을 처치했습니다!");
        }
        return isDefeated;
    }

    public void handleEnemyDefeat(GameState gameState) {
        validateGameState(gameState);

        int currentRound = gameState.getCurrentRound();

        // 다음 라운드로 진행
        gameState.nextRound();
        gameState.resetTurn();

        // 새로운 적 설정
        EnemyInfo nextEnemy = getEnemyForRound(gameState.getCurrentRound());
        gameState.setEnemyHp(nextEnemy.getHp());

        log.info("라운드 {}에서 라운드 {}로 진행합니다. 새로운 적: {} (체력: {})",
                currentRound, gameState.getCurrentRound(), nextEnemy.name(), nextEnemy.getHp());
    }

    public void processTurnAndEnemyAttack(GameState gameState, EnemyInfo enemy, boolean enemyDefeated) {
        validateTurnProcessingInputs(gameState, enemy);

        gameState.incremnetTurn();

        if (!enemyDefeated) {
            checkAndExecuteEnemyAttack(gameState, enemy);
        }
    }

    private void checkAndExecuteEnemyAttack(GameState gameState, EnemyInfo enemy) {
        int currentTurn = gameState.getCurrentTurn();
        int attackTurn = enemy.getAttackTurn();

        // 공격 턴인지 확인
        if (currentTurn % attackTurn == 0) {
            log.info("적 공격 턴입니다! (현재 턴: {}, 공격 주기: {})", currentTurn, attackTurn);
            executeEnemyAttack(gameState, enemy);
        } else {
            int turnsUntilAttack = attackTurn - (currentTurn % attackTurn);
            log.info("적 공격까지 {}턴 남았습니다. (현재 턴: {}, 공격 주기: {})",
                    turnsUntilAttack, currentTurn, attackTurn);
        }
    }

    private void executeEnemyAttack(GameState gameState, EnemyInfo enemy) {
        int attackPower = enemy.getAttackPower();
        int currentPlayerHp = gameState.getPlayerHp();

        gameState.damagePlayer(attackPower);

        if (gameState.getPlayerHp() <= 0) {
            log.warn("플레이어가 쓰러졌습니다!");
        }
    }

    private EnemyInfo getEnemyForRound(int round) {
        try {
            return EnemyInfo.ofRound(round);
        } catch (Exception e) {
            log.error("라운드 {}에 해당하는 적을 찾을 수 없습니다.", round);
            throw new BusinessException(ErrorCode.INVALID_ROUND);
        }
    }

    private void validateDamageInput(GameState gameState, int damage) {
        if (Objects.isNull(gameState)) {
            throw new BusinessException(ErrorCode.INVALID_GAME_STATE);
        }
        if (damage < 0) {
            throw new IllegalArgumentException("데미지 값이 양수여야 합니다.");
        }
    }

    private void validateGameState(GameState gameState) {
        if (Objects.isNull(gameState)) {
            throw new BusinessException(ErrorCode.INVALID_GAME_STATE);
        }
    }

    private void validateTurnProcessingInputs(GameState gameState, EnemyInfo enemy) {
        if (Objects.isNull(gameState)) {
            throw new BusinessException(ErrorCode.INVALID_GAME_STATE);
        }
        if (Objects.isNull(enemy)) {
            throw new BusinessException(ErrorCode.INVALID_ENEMY_INFO);
        }
    }
}
