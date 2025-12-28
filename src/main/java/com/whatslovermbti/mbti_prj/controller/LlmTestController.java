package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.dto.llm.PlaceAtmosphereTestRequest;
import com.whatslovermbti.mbti_prj.service.llm.GeminiService;
import com.whatslovermbti.mbti_prj.service.llm.dto.PlaceAtmosphereResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test/llm")
public class LlmTestController {

    private final GeminiService geminiService;

    @PostMapping("/place-atmosphere")
    public ResponseEntity<PlaceAtmosphereResult> analyzePlaceAtmosphere(
            @RequestBody PlaceAtmosphereTestRequest request
    ) {
        PlaceAtmosphereResult result =
                geminiService.analyzePlaceAtmosphere(
                        request.getPlaceName(),
                        request.getAddress(),
                        request.getCategory()
                );

        return ResponseEntity.ok(result);
    }
}
