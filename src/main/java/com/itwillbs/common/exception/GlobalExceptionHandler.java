package com.itwillbs.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.itwillbs.common.response.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* =========================
       잘못된 요청
    ========================= */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(
            IllegalArgumentException e
    ) {
        log.error("INVALID_REQUEST", e);
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail("INVALID_REQUEST", e.getMessage()));
    }

    /* =========================
       데이터 없음
    ========================= */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            EntityNotFoundException e
    ) {
        log.error("ENTITY_NOT_FOUND", e);
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail("ENTITY_NOT_FOUND", e.getMessage()));
    }

    /* =========================
       그 외 예외 (최후 방어)
    ========================= */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("INTERNAL_SERVER_ERROR", e);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.fail("INTERNAL_SERVER_ERROR", "처리 중 오류가 발생했습니다."));
    }
    
    @ExceptionHandler(DuplicateProductReportException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateProductReport(DuplicateProductReportException e) {
        return ResponseEntity.badRequest()
            .body(ApiResponse.fail(ApiResponseCode.REPORT_DUPLICATE, e.getMessage()));
    }
      
}
