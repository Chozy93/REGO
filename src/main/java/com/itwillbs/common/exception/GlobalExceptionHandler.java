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

/*
 * 통합 글로벌 예외 처리기
 *
 * ✔ View / REST 컨트롤러 혼합 허용
 * ✔ 응답 타입(JSON / HTML) 기준 분기
 * ✔ SSR은 DOM 유지 + 모달 제어
 * ✔ REST는 ApiResponse JSON 반환
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /* =========================
       Validation 오류 (@Valid)
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

        return handleByResponseType(
                request,
                model,
                e,
                "VALIDATION_ERROR",
                message
        );
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
        return handleByResponseType(
                request,
                model,
                e,
                "ENTITY_NOT_FOUND",
                e.getMessage()
        );
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
        return handleByResponseType(
                request,
                model,
                e,
                "ACCESS_DENIED",
                e.getMessage()
        );
    }

    /* =========================
       잘못된 요청 / 비즈니스 오류
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
        return handleByResponseType(
                request,
                model,
                e,
                "INVALID_REQUEST",
                e.getMessage()
        );
    }

    /* =========================
       그 외 모든 예외 (최후 방어)
    ========================= */
    @ExceptionHandler(Exception.class)
    public Object handleException(
            Exception e,
            HttpServletRequest request,
            Model model
    ) {
        return handleByResponseType(
                request,
                model,
                e,
                "INTERNAL_SERVER_ERROR",
                "처리 중 오류가 발생했습니다."
        );
    }

    /* ==================================================
       응답 타입 기준 분기 (핵심)
    ================================================== */
    private Object handleByResponseType(
            HttpServletRequest request,
            Model model,
            Exception e,
            String errorCode,
            String message
    ) {
        log.error(errorCode, e);

        // ✅ JSON 응답이 필요한 요청
        if (isJsonRequest(request)) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail(errorCode, message));
        }

        // ✅ View(SSR) 요청 → 모달만 제어
        model.addAttribute("isErrorModalOpen", true);
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", message);

        return resolveViewName(request);
    }

    /* =========================
       JSON 요청 판별
    ========================= */
    private boolean isJsonRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String contentType = request.getContentType();
        String xhr = request.getHeader("X-Requested-With");

        return (accept != null && accept.contains("application/json"))
                || (contentType != null && contentType.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xhr);
    }

    /* =========================
       현재 View 유지
    ========================= */
    private String resolveViewName(HttpServletRequest request) {
        Object viewName = request.getAttribute("CURRENT_VIEW_NAME");
        return viewName != null ? viewName.toString() : "main/index";
    }
}
