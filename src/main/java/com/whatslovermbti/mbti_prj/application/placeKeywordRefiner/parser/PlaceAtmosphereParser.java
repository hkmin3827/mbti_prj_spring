package com.whatslovermbti.mbti_prj.application.placeKeywordRefiner.parser;

import com.whatslovermbti.mbti_prj.domain.placeKeywordRefiner.dto.PlaceAtmosphereResult;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceAtmosphereParser {

    private static final Pattern CSV_PATTERN =
            Pattern.compile("KEYWORDS:\\s*(.+)", Pattern.CASE_INSENSITIVE);

    private PlaceAtmosphereParser() {}

    public static PlaceAtmosphereResult parse(String raw) {
        Matcher matcher = CSV_PATTERN.matcher(raw);
        if (!matcher.find()) {
            throw new IllegalStateException("KEYWORDS 라인을 찾지 못함");
        }
        String csv = matcher.group(1);


        List<String> keywords =
                Arrays.stream(csv.split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .filter(s -> !s.isBlank())
                        .toList();

        if (keywords.isEmpty()) {
            throw new IllegalStateException("Gemini 분위기 키워드 파싱 실패");
        }


        return new PlaceAtmosphereResult(keywords);
    }
}
