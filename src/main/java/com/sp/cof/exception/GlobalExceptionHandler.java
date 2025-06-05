package com.sp.cof.exception;

import com.sp.cof.common.ErrorCode;
import com.sp.cof.domain.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> businessExceptionHandler(HttpServletRequest request, BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), errorCode.getCode(), errorCode.getDesc()));
    }

    @ExceptionHandler({
            NoHandlerFoundException.class, MethodArgumentTypeMismatchException.class, HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity<ApiResponse<Void>> noHandlerFoundExceptionHandler(HttpServletRequest request, Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), ErrorCode.NOT_FOUND_HANDLER.getCode(), ErrorCode.NOT_FOUND_HANDLER.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> defaultExceptionHandler(HttpServletRequest request, Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.ETC.getCode(), ErrorCode.ETC.getMessage()));
    }
}
