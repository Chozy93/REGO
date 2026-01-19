package com.itwillbs.common.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
 * View(SSR) 전용 글로벌 예외 처리기
 *
 * - JSON 응답 ❌
 * - 페이지 이동 ❌
 * - 모달 제어용 상태만 Model에 주입
 * - Service는 예외만 throw
 * - View는 현재 페이지 그대로 유지
 */
@ControllerAdvice(annotations = Controller.class)
public class ViewGlobalExceptionHandler {

    /* =========================
       데이터 없음 (조회 실패)
    ========================= */
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFound(
            EntityNotFoundException e,
            Model model,
            HttpServletRequest request
    ) {
        return handleModalError(
                e,
                model,
                request,
                "ENTITY_NOT_FOUND"
        );
    }

    /* =========================
       권한 없음
    ========================= */
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(
            AccessDeniedException e,
            Model model,
            HttpServletRequest request
    ) {
        return handleModalError(
                e,
                model,
                request,
                "ACCESS_DENIED"
        );
    }

    /* =========================
       잘못된 상태 / 비즈니스 오류
    ========================= */
    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public String handleBadRequest(
            RuntimeException e,
            Model model,
            HttpServletRequest request
    ) {
        return handleModalError(
                e,
                model,
                request,
                "INVALID_REQUEST"
        );
    }

    /* =========================
       처리되지 않은 모든 예외
    ========================= */
    @ExceptionHandler(Exception.class)
    public String handleException(
            Exception e,
            Model model,
            HttpServletRequest request
    ) {
        return handleModalError(
                e,
                model,
                request,
                "INTERNAL_ERROR"
        );
    }

    /* =========================
       공통 모달 처리
    ========================= */
    private String handleModalError(
            Exception e,
            Model model,
            HttpServletRequest request,
            String errorCode
    ) {
        model.addAttribute("isErrorModalOpen", true);
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", e.getMessage());

        return resolveViewName(request);
    }

    /* =========================
       현재 View 유지
    ========================= */
    private String resolveViewName(HttpServletRequest request) {
        Object viewName = request.getAttribute("CURRENT_VIEW_NAME");
        return viewName != null ? viewName.toString() : "main/index";
    }
}
