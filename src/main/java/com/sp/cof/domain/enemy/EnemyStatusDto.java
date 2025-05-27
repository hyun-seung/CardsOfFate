package com.sp.cof.domain.enemy;

import com.sp.cof.domain.game.GameState;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "적 상태 정보")
public record EnemyStatusDto(

        @Schema(description = "공격력", example = "4")
        int attackerPower,

        @Schema(description = "적 HP", example = "300")
        int hp,

        @Schema(description = "공격까지 남은 턴 수", example = "3")
        int turnsUntilAttack
) {

    public static EnemyStatusDto from(EnemyInfo enemyInfo, GameState gameState) {
        int attackInterval = enemyInfo.getAttackTurn();
        int currentTurn = gameState.getCurrentTurn();

        int turnsUntilAttack = attackInterval - (currentTurn % attackInterval);
        if (turnsUntilAttack == attackInterval) {
            turnsUntilAttack = 0;
        }

        return new EnemyStatusDto(
                enemyInfo.getAttackPower(),
                gameState.getEnemyHp(),
                turnsUntilAttack
        );
    }
}
