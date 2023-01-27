package com.github.kingwaggs.ordermanager.domain;

import com.github.kingwaggs.ordermanager.util.DateTimeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) {
        String now = DateTimeConverter.localDateTime2DateTimeString(LocalDateTime.now());
        log.info("Request : {} {}, Requested At: {}", request.getMethod(), request.getRequestURL(), now);
        return true;
    }

}
