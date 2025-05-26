package com.sp.cof.domain.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "API 에러 상세 정보")
public class ApiError {

    @Schema(description = "에러 코드", example = "1001")
    private final int code;

    @Schema(description = "에러 메시지", example = "게임 ID가 유효하지 않습니다.")
    private final String message;
}
