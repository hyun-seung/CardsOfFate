package com.sp.cof.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "공통 API 응답 포맷")
public class ApiResponse<T> {

    @Schema(description = "HTTP 상태 코드", example = "200")
    private final int status;

    @Schema(description = "요청 성공 시 반환되는 데이터. 실패 시 null입니다.")
    private final T data;

    @Schema(description = "요청 실패 시 포함되는 에러 정보. 성공 시 null입니다.")
    private final ApiError error;

    private ApiResponse(int status, T data, ApiError error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, data, null);
    }

    public static <T> ApiResponse<T> error(int status, int code, String message) {
        return new ApiResponse<>(status, null, new ApiError(code, message));
    }

}
