package com.whatslovermbti.mbti_prj.security.jwt;

import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();

        return uri.startsWith("/auth/login")
                || uri.startsWith("/auth/signup")
                || uri.startsWith("/oauth2/")
                || uri.startsWith("/login/oauth2/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null) {
            try {
                Long userId = jwtProvider.getUserId(token);
                CustomUserDetails userDetails =
                        (CustomUserDetails) userDetailsService.loadUserById(userId);
                User user = userDetails.getUser();

                LocalDateTime lastLogoutAt = user.getLastLogoutAt();
                Date issuedAt = jwtProvider.getIssuedAt(token);

                if (lastLogoutAt != null) {
                    Instant issuedInstant = issuedAt.toInstant();
                    Instant logoutInstant =
                            lastLogoutAt.atZone(ZoneId.systemDefault()).toInstant();

                    if (issuedInstant.isBefore(logoutInstant)) {
                        throw new JwtException("로그아웃 이후 발급된 토큰");
                    }
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JwtException | IllegalArgumentException e) {
                // JWT 만료 / 위조 / 파싱 실패 → 401
                sendUnauthorizedResponse(response);
                return;
            }
        }


        filterChain.doFilter(request, response);

    }

    private String resolveToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }


    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write("""
            {
              "code": "UNAUTHORIZED",
              "message": "유효하지 않거나 만료된 토큰입니다."
            }
            """);
    }
}