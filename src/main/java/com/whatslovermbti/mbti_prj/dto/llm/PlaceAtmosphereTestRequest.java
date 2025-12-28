package com.whatslovermbti.mbti_prj.dto.llm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceAtmosphereTestRequest {

    private String placeName;
    private String address;
    private String category;
}