package com.sp.cof.domain.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class GameState {

    private final String gameId;

    private int currentRound = 1;

    private int playerHp = 100;

    private int currentTurn = 1;

    @Setter
    private int enemyHp;

    public void incremnetTurn() {
        this.currentTurn += 1;
    }

    public void nextRound() {
        this.currentRound += 1;
    }

    public void resetTrun() {
        this.currentTurn = 1;
    }

    public void damagePlayer(int damage) {
        this.playerHp = Math.max(0, playerHp - damage);
    }

    @Override
    public String toString() {
        return "GameState{" +
                "gameId='" + gameId + '\'' +
                ", currentRound=" + currentRound +
                ", playerHp=" + playerHp +
                ", currentTurn=" + currentTurn +
                ", enemyHp=" + enemyHp +
                '}';
    }
}
