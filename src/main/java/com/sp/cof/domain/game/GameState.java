package com.sp.cof.domain.game;

import com.sp.cof.common.Constant;
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

    private int discardRemainingThisRound;

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
