package com.itwillbs.common.exception;

import com.itwillbs.common.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /* =========================
       Validation 오류
    ========================= */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidation(
            MethodArgumentNotValidException e,
            HttpServletRequest request,
            Model model
    ) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("입력값을 확인해주세요.");

        return handle(request, model, e, "VALIDATION_ERROR", message);
    }

    /* =========================
       데이터 없음
    ========================= */
    @ExceptionHandler(EntityNotFoundException.class)
    public Object handleNotFound(
            EntityNotFoundException e,
            HttpServletRequest request,
            Model model
    ) {
        return handle(request, model, e, "ENTITY_NOT_FOUND", e.getMessage());
    }

    /* =========================
       권한 없음
    ========================= */
    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDenied(
            AccessDeniedException e,
            HttpServletRequest request,
            Model model
    ) {
        return handle(request, model, e, "ACCESS_DENIED", e.getMessage());
    }

    /* =========================
       잘못된 요청
    ========================= */
    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public Object handleBadRequest(
            RuntimeException e,
            HttpServletRequest request,
            Model model
    ) {
        return handle(request, model, e, "INVALID_REQUEST", e.getMessage());
    }

    /* =========================
       그 외 모든 예외
    ========================= */
    @ExceptionHandler(Exception.class)
    public Object handleException(
            Exception e,
            HttpServletRequest request,
            Model model
    ) {
        return handle(
                request,
                model,
                e,
                "INTERNAL_SERVER_ERROR",
                "처리 중 오류가 발생했습니다."
        );
    }

    /* =========================
       공통 처리
    ========================= */
    private Object handle(
            HttpServletRequest request,
            Model model,
            Exception e,
            String errorCode,
            String message
    ) {
        log.error(errorCode, e);

        // ✅ AJAX / REST → JSON
        if (isJsonRequest(request)) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail(errorCode, message));
        }

        // ✅ SSR → 에러 페이지
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", message);

        return "error/error-page";
    }

    /* =========================
       JSON 요청 판별
    ========================= */
    private boolean isJsonRequest(HttpServletRequest request) {
        String xhr = request.getHeader("X-Requested-With");
        String contentType = request.getContentType();

        if ("XMLHttpRequest".equalsIgnoreCase(xhr)) {
            return true;
        }

        return contentType != null && contentType.contains("application/json");
    }
}
