package com.itwillbs.common.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StaticResourceExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public void handleNoResourceFound(NoResourceFoundException e) {
        // 아무것도 하지 않음
        // 로그 X
        // 응답 바디 X
        // 그냥 404로 종료
    }
}
