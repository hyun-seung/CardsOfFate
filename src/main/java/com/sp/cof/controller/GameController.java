package com.sp.cof.controller;

import com.sp.cof.domain.record.GameInfo;
import com.sp.cof.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RequestMapping("/api/v1/game")
@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<GameInfo> gameStart() {
        String gameId = UUID.randomUUID().toString();
        long seed = Instant.now().toEpochMilli();
        log.info("[{}] Game Start! Seed = {}", gameId, seed);

        GameInfo gameInfo = gameService.startGame(gameId, seed);
        return ResponseEntity.ok(gameInfo);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameInfo> getGameInfo(@PathVariable String gameId) {
        GameInfo gameInfo = gameService.getGameInfo(gameId);
        return ResponseEntity.ok(gameInfo);
    }
}
