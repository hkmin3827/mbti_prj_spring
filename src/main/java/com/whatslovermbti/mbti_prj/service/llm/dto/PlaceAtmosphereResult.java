package com.whatslovermbti.mbti_prj.service.llm.dto;

import java.util.List;

public record PlaceAtmosphereResult(
        List<String> rawKeywords   // LLM이 말한 그대로
) {}