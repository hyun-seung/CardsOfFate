package com.sp.cof.service;

import com.sp.cof.common.Constant;
import com.sp.cof.domain.enemy.EnemyInfo;
import com.sp.cof.domain.game.GameState;
import com.sp.cof.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnemyServiceTest {

    private EnemyService enemyService;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        enemyService = new EnemyService();
        gameState = new GameState("test-game");
        gameState.setEnemyHp(100);
    }

    @DisplayName("적에게 데미지를 적용한다.")
    @Test
    void applyDamageToEnemy_Success() {
        // given
        int damage = 30;
        int originHp = gameState.getEnemyHp();

        // when
        boolean isDefeated = enemyService.applyDamageToEnemy(gameState, damage);

        // then
        assertThat(gameState.getEnemyHp()).isEqualTo(originHp - damage);
        assertThat(isDefeated).isFalse();
    }

    @DisplayName("데미지가 적 체력보다 클 때 적이 처치된다.")
    @Test
    void applyDamageToEnemy_EnemyDefeated() {
        // given
        int damage = 150;

        // when
        boolean isDefeated = enemyService.applyDamageToEnemy(gameState, damage);

        // then
        assertThat(gameState.getEnemyHp()).isEqualTo(0);
        assertThat(isDefeated).isTrue();
    }

    @DisplayName("데미지가 정확히 적 체력과 같을 때 적이 처치된다")
    @Test
    void applyDamageToEnemy_ExactDamage() {
        // given
        int damage = 100;

        // when
        boolean isDefeated = enemyService.applyDamageToEnemy(gameState, damage);

        // then
        assertThat(gameState.getEnemyHp()).isEqualTo(0);
        assertThat(isDefeated).isTrue();
    }

    @DisplayName("데미지가 0일 때 적 체력이 변하지 않는다.")
    @Test
    void applyDamageToEnemy_ZeroDamage() {
        // given
        int damage = 0;
        int originalHp = gameState.getEnemyHp();

        // when
        boolean isDefeated = enemyService.applyDamageToEnemy(gameState, damage);

        // then
        assertThat(gameState.getEnemyHp()).isEqualTo(originalHp);
        assertThat(isDefeated).isFalse();
    }

    @Test
    @DisplayName("데미지가 음수일 때 예외를 발생시킨다")
    void applyDamageToEnemy_NegativeDamage_ThrowsException() {
        // given
        int negativeDamage = -10;

        // when & then
        assertThatThrownBy(() -> enemyService.applyDamageToEnemy(gameState, negativeDamage))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("데미지 값이 양수여야 합니다");
    }

    @Test
    @DisplayName("GameState가 null일 때 예외를 발생시킨다")
    void applyDamageToEnemy_NullGameState_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> enemyService.applyDamageToEnemy(null, 10))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("적 처치 시 다음 라운드로 정상적으로 진행한다")
    void handleEnemyDefeat_Success() {
        // given
        int originalRound = gameState.getCurrentRound();
        // 턴을 여러 번 증가시켜 1이 아닌 상태로 만듦
        gameState.incremnetTurn();
        gameState.incremnetTurn();
        gameState.incremnetTurn();
        assertThat(gameState.getCurrentTurn()).isNotEqualTo(1); // 전제 조건 확인

        // when
        enemyService.handleEnemyDefeat(gameState);

        // then
        assertThat(gameState.getCurrentRound()).isEqualTo(originalRound + 1);
        assertThat(gameState.getCurrentTurn()).isEqualTo(1); // 턴이 리셋됨
        assertThat(gameState.getEnemyHp()).isGreaterThan(0); // 새로운 적의 체력 설정됨
    }

    @Test
    @DisplayName("라운드 1에서 라운드 2로 진행할 때 적절한 적이 설정된다")
    void handleEnemyDefeat_Round1ToRound2() {
        // given
        assertThat(gameState.getCurrentRound()).isEqualTo(1); // 초기 라운드 확인

        // when
        enemyService.handleEnemyDefeat(gameState);

        // then
        assertThat(gameState.getCurrentRound()).isEqualTo(2);
        EnemyInfo expectedEnemy = EnemyInfo.ofRound(2);
        assertThat(gameState.getEnemyHp()).isEqualTo(expectedEnemy.getHp());
    }

    @Test
    @DisplayName("적 처치 시 버리기 횟수도 리셋된다")
    void handleEnemyDefeat_DiscardReset() {
        // given
        gameState.useDiscard(); // 버리기 사용
        gameState.useDiscard(); // 버리기 사용
        int originalDiscardRemaining = gameState.getDiscardRemainingThisRound();
        assertThat(originalDiscardRemaining).isLessThan(Constant.MAX_DISCARD_PER_ROUND);

        // when
        enemyService.handleEnemyDefeat(gameState);

        // then
        assertThat(gameState.getDiscardRemainingThisRound()).isEqualTo(Constant.MAX_DISCARD_PER_ROUND);
    }

    @Test
    @DisplayName("handleEnemyDefeat에서 GameState가 null일 때 예외를 발생시킨다")
    void handleEnemyDefeat_NullGameState_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> enemyService.handleEnemyDefeat(null))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("적이 처치되지 않았을 때 턴을 증가시킨다")
    void processTurnAndEnemyAttack_IncrementTurn() {
        // given
        EnemyInfo enemy = EnemyInfo.ROUND_1;
        int originalTurn = gameState.getCurrentTurn();
        boolean enemyDefeated = false;

        // when
        enemyService.processTurnAndEnemyAttack(gameState, enemy, enemyDefeated);

        // then
        assertThat(gameState.getCurrentTurn()).isEqualTo(originalTurn + 1);
    }

    @Test
    @DisplayName("적이 처치되었을 때도 턴은 증가한다")
    void processTurnAndEnemyAttack_EnemyDefeated_TurnIncreases() {
        // given
        EnemyInfo enemy = EnemyInfo.ROUND_1;
        int originalTurn = gameState.getCurrentTurn();
        boolean enemyDefeated = true;

        // when
        enemyService.processTurnAndEnemyAttack(gameState, enemy, enemyDefeated);

        // then
        assertThat(gameState.getCurrentTurn()).isEqualTo(originalTurn + 1);
    }

    @Test
    @DisplayName("적이 처치되었을 때는 공격 턴이어도 플레이어가 공격받지 않는다")
    void processTurnAndEnemyAttack_EnemyDefeated_EvenOnAttackTurn_NoPlayerDamage() {
        // given
        EnemyInfo enemy = EnemyInfo.ROUND_1;
        int attackTurn = enemy.getAttackTurn();

        // 공격 턴이 되도록 설정
        for (int i = 1; i < attackTurn; i++) {
            gameState.incremnetTurn();
        }
        // 이제 다음 턴이 공격 턴

        int originalPlayerHp = gameState.getPlayerHp();
        boolean enemyDefeated = true;

        // when
        enemyService.processTurnAndEnemyAttack(gameState, enemy, enemyDefeated);

        // then
        assertThat(gameState.getCurrentTurn()).isEqualTo(attackTurn + 1); // 공격 턴임을 확인
        assertThat(gameState.getPlayerHp()).isEqualTo(originalPlayerHp); // 하지만 공격받지 않음
    }
}