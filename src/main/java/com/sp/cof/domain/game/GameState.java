package com.sp.cof.domain.game;

import com.sp.cof.common.Constant;
import lombok.Getter;
import lombok.Setter;

@Getter
public class GameState {

    private final String gameId;

    private int currentRound;

    private int playerHp;

    private int currentTurn;

    private int discardRemainingThisRound;

    @Setter
    private int enemyHp;

    public GameState(String gameId) {
        this.gameId = gameId;
        this.currentRound = 1;
        this.playerHp = Constant.PLAYER_MAX_HP;
        this.currentTurn = 1;
        this.discardRemainingThisRound = Constant.MAX_DISCARD_PER_ROUND;
    }

    public void incremnetTurn() {
        this.currentTurn += 1;
    }

    public void nextRound() {
        this.currentRound += 1;
    }

    public void resetTurn() {
        this.currentTurn = 1;
        this.discardRemainingThisRound = Constant.MAX_DISCARD_PER_ROUND;
    }

    public void damagePlayer(int damage) {
        this.playerHp = Math.max(0, playerHp - damage);
    }

    public boolean canDiscard() {
        return discardRemainingThisRound > 0;
    }

    public void useDiscard() {
        if (canDiscard()) {
            discardRemainingThisRound -= 1;
        }
    }

    @Override
    public String toString() {
        return "GameState{" +
                "gameId='" + gameId + '\'' +
                ", currentRound=" + currentRound +
                ", playerHp=" + playerHp +
                ", currentTurn=" + currentTurn +
                ", discardRemainingThisRound=" + discardRemainingThisRound +
                ", enemyHp=" + enemyHp +
                '}';
    }
}
