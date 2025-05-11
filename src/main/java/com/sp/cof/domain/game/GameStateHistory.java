package com.sp.cof.domain.game;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GameStateHistory {
    private Long id;
    private String gameId;
    private int round;
    private int turn;
    private int playerHp;
    private String handJson;
    private int enemyHp;
    private LocalDateTime createdAt;

    @Builder
    public GameStateHistory(String gameId, int round, int turn, int playerHp, String handJson, int enemyHp) {
        this.gameId = gameId;
        this.turn = turn;
        this.round = round;
        this.playerHp = playerHp;
        this.handJson = handJson;
        this.enemyHp = enemyHp;
    }

    @Override
    public String toString() {
        return "GameStateHistory{" +
                "id=" + id +
                ", gameId='" + gameId + '\'' +
                ", round=" + round +
                ", playerHp=" + playerHp +
                ", handJson='" + handJson + '\'' +
                ", enemyHp=" + enemyHp +
                ", createdAt=" + createdAt +
                '}';
    }
}
