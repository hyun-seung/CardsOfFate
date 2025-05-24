package com.sp.cof.controller;

import com.sp.cof.domain.common.ApiResponse;
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
    public ResponseEntity<ApiResponse<GameStatusDto>> gameStart() {
        String gameId = UUID.randomUUID().toString();
        long seed = Instant.now().toEpochMilli();
        log.info("[{}] Game Start! Seed = {}", gameId, seed);

        GameStatusDto gameStatusDto = gameService.startGame(gameId, seed);
        return ResponseEntity.ok(ApiResponse.success(gameStatusDto));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<ApiResponse<GameStatusDto>> getGameStatus(@PathVariable String gameId) {
        GameStatusDto gameStatusDto = gameService.getGameStatus(gameId);
        return ResponseEntity.ok(ApiResponse.success(gameStatusDto));
    }

    @PostMapping("/process")
    public ResponseEntity<ApiResponse<GameStatusDto>> processTurn(@RequestBody TurnRequestDto turnRequestDto) {
        log.info("GameId : {}, Action : {}, Cards : {}",
                turnRequestDto.gameId(), turnRequestDto.actionType(), turnRequestDto.playedCards());

        GameStatusDto result = switch (turnRequestDto.actionType()) {
            case ATTACk -> gameService.processTurn(turnRequestDto.gameId(), turnRequestDto.playedCards());
            case DISCARD -> gameService.discardCards(turnRequestDto.gameId(), turnRequestDto.playedCards());
        };
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
