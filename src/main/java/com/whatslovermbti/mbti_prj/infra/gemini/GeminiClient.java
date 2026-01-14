package com.whatslovermbti.mbti_prj.infra.gemini;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final WebClient geminiWebClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model.name}")
    private String modelName;

    @Value("${gemini.model.temperature}")
    private double temperature;

    @Value("${gemini.model.max-output-tokens}")
    private int maxOutputTokens;

    public String generate(String prompt) {

        Map<String, Object> body =
                Map.of(
                        "contents", List.of(
                                Map.of(
                                        "role", "user",
                                        "parts", List.of(
                                                Map.of("text", prompt)
                                        )
                                )
                        ),
                        "generationConfig", Map.of(
                                "temperature", temperature,
                                "maxOutputTokens", maxOutputTokens
                        )
                );

        log.info("[Gemini] request start");

        return geminiWebClient.post()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/v1beta/models/{model}:generateContent")
                                .queryParam("key", apiKey)
                                .build(modelName)
                )
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(
                        Retry.backoff(1, Duration.ofMillis(300))
                                .filter(e ->
                                        e instanceof WebClientResponseException.TooManyRequests ||
                                                e instanceof WebClientResponseException.ServiceUnavailable
                                )
                )
                .doOnSuccess(r -> log.info("[Gemini] request success"))
                .doOnError(e -> log.error("[Gemini] request failed", e))
                .block();
    }
}
