package com.xkb.couple.core.interceptor;

import com.xkb.couple.core.common.constants.SystemConfigConstans;
import com.xkb.couple.core.utils.JwtUtil;
import com.xkb.couple.core.utils.UserHolder;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 登录拦截器
 *  @author xuwatermelon
 *  @date 2026/02/06
 */
@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("NullableProblems")
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    // Token前缀（可放到常量类中）

    @SuppressWarnings("RedundantThrows")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 非Controller方法直接放行（如静态资源）
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }



        // 3. 获取并处理Token
        String token = request.getHeader(SystemConfigConstans.HTTP_AUTHORIZATION);
        if (!StringUtils.hasText(token)) {
            log.warn("请求[{}]缺少Authorization头", request.getRequestURI());
            failureResponse(response, HttpStatus.UNAUTHORIZED.value(), "未授权访问，请先登录");
            return false;
        }

        // 4. 剥离Token前缀（兼容Bearer格式）
        if (token.startsWith(SystemConfigConstans.TOKEN_PREFIX)) {
            token = token.substring(SystemConfigConstans.TOKEN_PREFIX.length()).trim();
        }

        // 5. 解析Token（细化异常类型）
        Long userId;
        try {
            userId = jwtUtil.parseToken(token);
            if (userId == null) {
                log.warn("JWT解析无用户ID: {}", maskToken(token));
                failureResponse(response, HttpStatus.UNAUTHORIZED.value(), "Token无效，请重新登录");
                return false;
            }
        } catch (ExpiredJwtException e) {
            log.warn("JWT已过期: {}", maskToken(token));
            failureResponse(response, HttpStatus.UNAUTHORIZED.value(), "Token已过期，请重新登录");
            return false;
        } catch (SignatureException e) {
            log.warn("JWT签名错误: {}", maskToken(token));
            failureResponse(response, HttpStatus.UNAUTHORIZED.value(), "Token签名错误，可能被篡改");
            return false;
        } catch (MalformedJwtException e) {
            log.warn("JWT格式错误: {}", maskToken(token));
            failureResponse(response, HttpStatus.UNAUTHORIZED.value(), "Token格式错误，请检查");
            return false;
        } catch (Exception e) {
            log.error("JWT验证异常", e);
            failureResponse(response, HttpStatus.UNAUTHORIZED.value(), "Token验证失败");
            return false;
        }

        // 6. 存储用户ID到ThreadLocal（移除错误的校验逻辑）
        UserHolder.setUserId(userId);
        log.info("用户[{}]请求[{}]已通过鉴权", userId, request.getRequestURI());

        // 7. 优化Token刷新逻辑：仅当Token快过期时刷新（需要JwtUtil支持过期时间判断）
        if (jwtUtil.isTokenExpiringSoon(token)) {
            String newToken = jwtUtil.generateToken(userId);
            response.setHeader(SystemConfigConstans.HTTP_AUTHORIZATION, SystemConfigConstans.TOKEN_PREFIX + newToken);
            log.info("用户[{}]Token即将过期，已刷新", userId);
        }

        return true;
    }
    @SuppressWarnings("RedundantThrows")
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        // 必须清理ThreadLocal，防止内存泄漏
        UserHolder.removeUserId();
        log.debug("请求[{}]完成，已清理ThreadLocal", request.getRequestURI());
    }



    /**
     * 构建失败响应
     */
    private void failureResponse(HttpServletResponse response, int code, String message) {
        try {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");

            Map<String, Object> responseBody = new HashMap<>(2);
            responseBody.put("code", code);
            responseBody.put("message", message);

            objectMapper.writeValue(response.getWriter(), responseBody);
        } catch (IOException e) {
            log.error("构建失败响应异常", e);
        }
    }

    /**
     * 脱敏Token（避免日志泄露完整Token）
     */
    private String maskToken(String token) {
        if (!StringUtils.hasText(token) || token.length() <= SystemConfigConstans.TOKEN_MASK_LENGTH) {
            return token;
        }
        return token.substring(0, 20) + "...";
    }
}