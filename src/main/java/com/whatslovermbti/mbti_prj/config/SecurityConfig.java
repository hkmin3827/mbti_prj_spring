package com.whatslovermbti.mbti_prj.config;

import com.whatslovermbti.mbti_prj.handler.OAuth2LoginSuccessHandler;
import com.whatslovermbti.mbti_prj.security.jwt.JwtAuthenticationFilter;
import com.whatslovermbti.mbti_prj.security.jwt.JwtProvider;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtProvider jwtProvider;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
//                                "/",
//                                "/login",
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/test/places/**",
                                "/api/test/llm/**"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/api/places/**",
                                "/api/users/**",
                                "/api/recommend/**",
                                "/api/views/**",
                                "/api/reactions/**",
                                "/api/bookmarks/**",
                                "/api/keywords/**",
                                "/api/test/ocr/**",
                                "/api/s3/**",
                                "/api/reviews/**",
                                "/api/profile/**"
                        ).authenticated()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable())


                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                        ).accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value()); // 403
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("""
                            {
                              "code": "ACCESS_DENIED",
                              "message": "접근 권한이 없습니다."
                            }
                            """);
                        })
                )

                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtProvider, customUserDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                ).oauth2Login(oauth -> oauth
                        .successHandler(oAuth2LoginSuccessHandler)
                );

        return http.build();
    }
}
