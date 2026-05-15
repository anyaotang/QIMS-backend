package com.qims.common.security;

import com.qims.common.config.JwtProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        // 支持两种方式：Header 或 URL 参数
        if (token == null) {
            token = request.getParameter("token");
        }

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.debug("Set authentication for user: {}", auth.getName());

            // 将 userId 写入 request attribute，供 Controller 直接获取
            try {
                var claims = jwtTokenProvider.parseToken(token);
                Object userId = claims.get("userId");
                if (userId != null) {
                    request.setAttribute("userId", ((Number) userId).longValue());
                }
                // 同时放入 username、departmentId、roles
                request.setAttribute("username", claims.get("username"));
                Object deptId = claims.get("departmentId");
                if (deptId != null) {
                    request.setAttribute("departmentId", ((Number) deptId).longValue());
                }
                // 设置数据权限上下文
                Long userIdVal = userId != null ? ((Number) userId).longValue() : null;
                Long deptIdVal = deptId != null ? ((Number) deptId).longValue() : null;
                Object dataScopeObj = claims.get("dataScope");
                Integer dataScope = dataScopeObj != null ? ((Number) dataScopeObj).intValue() : 4;
                com.qims.common.datascope.DataScopeContext.set(userIdVal, deptIdVal, dataScope);
            } catch (Exception e) {
                log.warn("Failed to extract JWT claims to request attributes: {}", e.getMessage());
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            com.qims.common.datascope.DataScopeContext.clear();
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProperties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getPrefix())) {
            return bearerToken.substring(jwtProperties.getPrefix().length());
        }
        return null;
    }
}
