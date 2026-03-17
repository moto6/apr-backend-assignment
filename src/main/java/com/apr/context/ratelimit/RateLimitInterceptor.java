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

        // 설계 명세 3번: 식별 전략 (X-User-Id 우선)
        String userId = request.getHeader("X-User-Id");
        String key = (userId != null) ? "CID:" + userId : "IP:" + request.getRemoteAddr();

        if (!rateLimiterService.isAllowed(key, rateLimit.value())) {
            // 설계 명세 10번: 429 Too Many Requests 반환
//            response.setStatus(429);
//            response.getWriter().write("Too Many Requests - Limit 10 per second");
//            return false;
            throw new RateLimitExceededException(rateLimit.value());
        }

        return true;
    }
}