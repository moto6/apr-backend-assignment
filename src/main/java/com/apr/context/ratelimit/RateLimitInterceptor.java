package com.apr.context.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiterService rateLimiterService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) return true;

        RateLimit rateLimit = handlerMethod.getMethodAnnotation(RateLimit.class);
        if (rateLimit == null) return true;
        String userId = request.getHeader("X-User-Id");
        String key = (userId != null) ? "CID:" + userId : "IP:" + request.getRemoteAddr();

        if (!rateLimiterService.isAllowed(key, rateLimit.value())) {
            throw new RateLimitExceededException(rateLimit.value());
        }

        return true;
    }
}