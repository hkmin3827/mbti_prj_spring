package com.whatslovermbti.mbti_prj.interfaces;


import com.whatslovermbti.mbti_prj.domain.keyword.entity.Keyword;
import com.whatslovermbti.mbti_prj.application.keyword.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/keywords")
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping
    public List<Keyword> getAll() {
        return keywordService.getAllKeywords();
    }
}