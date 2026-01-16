package com.whatslovermbti.mbti_prj.security.jwt;
// 설정 값 담당
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")   // application.yml의 jwt 설정을 자동 바인딩
@Component   // 스프링이 자동으로 빈 등록
@Getter
@Setter
public class JwtProperties {
    private String secret;
    
    // 유효 시간 : 1일 (1000L*60*60*24)
    private long expiration;
}