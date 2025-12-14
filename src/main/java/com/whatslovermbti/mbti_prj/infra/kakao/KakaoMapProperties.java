package com.whatslovermbti.mbti_prj.infra.kakao;
// application.yml 값 -> 타입 안전하게 주입
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kakao.map")
@Getter @Setter
public class KakaoMapProperties {
    private String restApiKey;
}