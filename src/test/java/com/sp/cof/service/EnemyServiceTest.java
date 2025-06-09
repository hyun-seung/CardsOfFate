package com.sp.cof.service;

import com.sp.cof.domain.game.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.assertj.core.api.Assertions.assertThat;

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
}