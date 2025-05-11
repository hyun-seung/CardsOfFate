package com.sp.cof.domain.enemy;

public record EnemyStatusDto(
        int attackerPower,
        int hp,
        int turnsUntilAttack
) {
    public static EnemyStatusDto from(EnemyInfo enemyInfo) {
        return new EnemyStatusDto(
                enemyInfo.getAttackPower(),
                enemyInfo.getHp(),
                enemyInfo.getTurnsUntilAttack()
        );
    }

}
