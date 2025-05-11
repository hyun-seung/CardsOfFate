package com.sp.cof.domain.enemy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EnemyInfo {

    ROUND_1(1, 3, 4, 300),
    ROUND_2(2, 3, 5, 600),
    ROUND_3(3, 2, 6, 1_000),
    ROUND_4(4, 2, 7, 1_500),
    ROUND_5(5, 2, 8, 2_000),
    ROUND_6(6, 1, 9, 2_600),
    ROUND_7(7, 1, 9, 3_200),
    ROUND_8(8, 1, 10, 3_800),
    ROUND_9(9, 1, 10, 4_400),
    ROUND_10(10, 1, 12, 5_000);

    private final int round;
    private final int turnsUntilAttack;
    private final int attackPower;
    private final int hp;

    public static EnemyInfo ofRound(int round) {
        return Arrays.stream(values())
                .filter(e -> e.name().equals("ROUND_" + round))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 round 정보입니다."));
    }
}
