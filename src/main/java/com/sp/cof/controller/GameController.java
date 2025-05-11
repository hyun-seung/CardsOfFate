package com.sp.cof.controller;

import com.sp.cof.domain.game.GameInfoDto;
import com.sp.cof.domain.game.GameStatusDto;
import com.sp.cof.domain.turn.TurnRequestDto;
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
    public ResponseEntity<GameStatusDto> gameStart() {
        String gameId = UUID.randomUUID().toString();
        long seed = Instant.now().toEpochMilli();
        log.info("[{}] Game Start! Seed = {}", gameId, seed);

        GameStatusDto gameStatusDto = gameService.startGame(gameId, seed);
        return ResponseEntity.ok(gameStatusDto);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameInfoDto> getGameInfo(@PathVariable String gameId) {
        GameInfoDto gameInfoDto = gameService.getGameInfo(gameId);
        return ResponseEntity.ok(gameInfoDto);
    }

    @PostMapping("/turn")
    public ResponseEntity<GameStatusDto> processTurn(@RequestBody TurnRequestDto turnRequestDto) {
        log.info("TurnRequestDto.playerCards : " + turnRequestDto.playedCards());
        GameStatusDto gameStatusDto = gameService.processTurn(turnRequestDto.gameId(), turnRequestDto.playedCards());
        return ResponseEntity.ok(gameStatusDto);
    }
}
