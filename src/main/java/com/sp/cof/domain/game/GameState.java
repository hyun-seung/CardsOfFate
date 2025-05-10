package com.sp.cof.domain.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GameState {
    private final String gameId;
    private int currentRound = 1;
    private int playerHp = 100;
    private int currentTurn = 1;

    @Override
    public String toString() {
        return "GameState{" +
                "gameId='" + gameId + '\'' +
                ", currentRound=" + currentRound +
                ", playerHp=" + playerHp +
                ", currentTurn=" + currentTurn +
                '}';
    }
}
