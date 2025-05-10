package com.sp.cof.controller;

import com.sp.cof.domain.record.GameInfo;
import com.sp.cof.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RequestMapping("/api/v1/game")
@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/start")
    public GameInfo gameStart() {
        String gameId = UUID.randomUUID().toString();
        long seed = Instant.now().toEpochMilli();
        log.info("[{}] Game Start! Seed = {}", gameId, seed);

        return gameService.startGame(gameId, seed);
    }
}
