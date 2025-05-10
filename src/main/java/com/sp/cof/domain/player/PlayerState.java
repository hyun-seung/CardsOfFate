package com.sp.cof.domain.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlayerState {

    private final String gameId;
    private int currentRound = 1;
    private int hp = 100;
}
