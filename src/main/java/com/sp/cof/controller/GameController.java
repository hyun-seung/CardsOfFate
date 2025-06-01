package com.sp.cof.controller;

import com.sp.cof.domain.card.Card;
import com.sp.cof.domain.common.ApiResponse;
import com.sp.cof.domain.game.GameStatusDto;
import com.sp.cof.domain.turn.TurnRequestDto;
import com.sp.cof.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequestMapping("/api/v1/game")
@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @Operation(summary = "게임 시작", description = "새로운 게임 세션을 생성하고 초기 상태를 반환합니다.")
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<GameStatusDto>> gameStart() {
        String gameId = UUID.randomUUID().toString();
        long seed = Instant.now().toEpochMilli();
        log.info("[{}] Game Start! Seed = {}", gameId, seed);

        GameStatusDto gameStatusDto = gameService.startGame(gameId, seed);
        return ResponseEntity.ok(ApiResponse.success(gameStatusDto));
    }

    @Operation(summary = "턴 진행 처리", description = "공격 또는 카드 버리기 액션을 처리하고 결과 게임 상태를 반환합니다.")
    @PostMapping("/process")
    public ResponseEntity<ApiResponse<GameStatusDto>> processTurn(@RequestBody TurnRequestDto turnRequestDto) {
        log.info("GameId : {}, Action : {}, Cards : {}",
                turnRequestDto.gameId(), turnRequestDto.actionType(), turnRequestDto.playedCards());

        GameStatusDto result = switch (turnRequestDto.actionType()) {
            case ATTACK -> gameService.processTurn(turnRequestDto.gameId(), turnRequestDto.playedCards());
            case DISCARD -> gameService.discardCards(turnRequestDto.gameId(), turnRequestDto.playedCards());
        };
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "게임 상태 조회", description = "특정 게임 ID에 대한 현재 게임 상태를 반환합니다.")
    @GetMapping("/{gameId}")
    public ResponseEntity<ApiResponse<GameStatusDto>> getGameStatus(@PathVariable String gameId) {
        GameStatusDto gameStatusDto = gameService.getGameStatus(gameId);
        return ResponseEntity.ok(ApiResponse.success(gameStatusDto));
    }

    @Operation(summary = "남은 덱 카드 조회", description = "현재 게임의 덱에 남아 있는 카드 리스트를 반환합니다.")
    @GetMapping("/deck/{gameId}")
    public ResponseEntity<ApiResponse<List<Card>>> getRemainingDeck(@PathVariable String gameId) {
        List<Card> remainingDeck = gameService.getRemainingDeck(gameId);
        return ResponseEntity.ok(ApiResponse.success(remainingDeck));
    }

}
