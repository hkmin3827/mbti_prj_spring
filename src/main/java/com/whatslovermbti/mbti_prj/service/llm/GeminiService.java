package com.whatslovermbti.mbti_prj.service.llm;

import com.whatslovermbti.mbti_prj.infra.gemini.GeminiClient;
import com.whatslovermbti.mbti_prj.service.llm.dto.PlaceAtmosphereResult;
import com.whatslovermbti.mbti_prj.service.llm.parser.PlaceAtmosphereParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    private final GeminiClient geminiClient;

    /**
     * 장소 분위기 분석 (텍스트 생성)
     */
    public PlaceAtmosphereResult analyzePlaceAtmosphere(
            String placeName,
            String address,
            String category
    ) {
        String prompt = """
                다음 장소의 분위기를 보고 아래 형식으로만 출력하라.
                
                [FORMAT]
                KEYWORDS: 키워드1, 키워드2, 키워드3, 키워드4, 키워드5
                
                [DATA]
                NAME: %s
                ADDRESS: %s
                CATEGORY: %s
                """.formatted(placeName, address, category);

        String raw = geminiClient.generate(prompt);
        log.info("[Gemini RAW]\n{}", raw);

        PlaceAtmosphereResult result =
                PlaceAtmosphereParser.parse(raw);


        log.info(
                "[Atmosphere Parsed] keywords={}",
                result.rawKeywords()
        );
        return result;
    }
}
