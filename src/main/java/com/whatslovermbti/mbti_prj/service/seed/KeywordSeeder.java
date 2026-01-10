package com.whatslovermbti.mbti_prj.service.seed;

import com.whatslovermbti.mbti_prj.constant.MbtiAxis;
import com.whatslovermbti.mbti_prj.entity.AppSeedHistory;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.repository.KeywordRepository;
import com.whatslovermbti.mbti_prj.repository.SeedHistoryRepository;
import com.whatslovermbti.mbti_prj.service.keyword.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1) // MbtiKeywordSeeder보다 먼저 실행
public class KeywordSeeder implements CommandLineRunner {

    private final KeywordRepository keywordRepository;
    private final SeedHistoryRepository seedHistoryRepository;
    private final KeywordService keywordService;

    @Override
    public void run(String... args) {
        if (seedHistoryRepository.existsById("KEYWORD")) {
            return;
        }

        /* ================= I ================= */
        keywordService.createKeywordWithNormalizations(
                "조용한", MbtiAxis.I,
                List.of(
                        "고즈넉한", "조용한", "차분한", "정적인",
                        "평화로운", "사색하기좋은", "혼자가기좋은"
                )
        );

        keywordService.createKeywordWithNormalizations(
                "아늑한", MbtiAxis.I,
                List.of(
                        "아늑한", "따스한", "몽글몽글한",
                        "포근한", "아지트같은", "비밀스러운"
                )
        );

        keywordService.createKeywordWithNormalizations(
                "프라이빗한", MbtiAxis.I,
                List.of(
                        "프라이빗한", "프라이빗", "조용히즐기는", "나만알고싶은"
                )
        );

        /* ================= E ================= */
        keywordService.createKeywordWithNormalizations(
                "화려한", MbtiAxis.E,
                List.of(
                        "화려한", "웅장한", "볼거리많은",
                        "사진맛집", "야경명소"
                )
        );

        keywordService.createKeywordWithNormalizations(
                "활동적인", MbtiAxis.E,
                List.of(
                        "시끌벅적한", "시끄러운", "활동적인", "활동적", "북적이는", "사람많은",
                        "활기찬", "생동감넘치는"
                )
        );

        keywordService.createKeywordWithNormalizations(
                "힙한", MbtiAxis.E,
                List.of(
                        "힙한", "트렌디한", "팝한",
                        "힙스터들의", "인스타감성"
                )
        );

        /* ================= N ================= */
        keywordService.createKeywordWithNormalizations(
                "감성적인", MbtiAxis.N,
                List.of(
                        "감성적인", "감성적", "감성", "몽환적인", "서정적인", "낭만적인",
                        "여행온기분", "이국적인"
                )
        );

        keywordService.createKeywordWithNormalizations(
                "감각적인", MbtiAxis.N,
                List.of(
                        "감각적인","감각적", "미학적인", "세련된",
                        "유니크한", "디자인좋은"
                )
        );

        keywordService.createKeywordWithNormalizations(
                "분위기좋은", MbtiAxis.N,
                List.of("분위기좋은", "분위기 좋은")
        );


        /* ================= S ================= */
        keywordService.createKeywordWithNormalizations(
                "가성비좋은", MbtiAxis.S,
                List.of(
                        "가성비", "가성 비", "가성비좋은", "실속있는", "저렴한", "저렴",
                        "숨겨진맛집"
                )
        );

        keywordService.createKeywordWithNormalizations(
                "정갈한", MbtiAxis.S,
                List.of(
                        "정갈한", "쾌적한",
                        "정돈된", "관리잘된"
                )
        );

        keywordService.createKeywordWithNormalizations(
                "실용적인", MbtiAxis.S,
                List.of("실용적인", "실용적", "실용", "효율적인")
        );


        /* ================= F ================= */
        keywordService.createKeywordWithNormalizations(
                "로맨틱한", MbtiAxis.F,
                List.of(
                        "로맨틱한", "데이트성지", "로맨틱", "데이트", "데이트하기 좋은", "데이트하기좋은",
                        "기념일추천", "연말분위기"
                )
        );

        keywordService.createKeywordWithNormalizations(
                "따뜻한", MbtiAxis.F,
                List.of(
                        "따뜻한", "친근한", "도란도란",
                        "부모님모시고", "가족나들이"
                )
        );

        /* ================= T ================= */
        keywordService.createKeywordWithNormalizations(
                "논리적인", MbtiAxis.T,
                List.of(
                        "논리적인", "체계적인", "논리적", "논리", "지적인",
                        "전문적인"
                )
        );

        keywordService.createKeywordWithNormalizations(
                "깔끔한", MbtiAxis.T,
                List.of(
                        "깔끔한", "미니멀한", "깔끔", "심플한"
                )
        );

        /* ================= J ================= */
        keywordService.createKeywordWithNormalizations(
                "계획적인", MbtiAxis.J,
                List.of(
                        "계획적인", "동선좋은",
                        "안정적인", "특별한날"
                )
        );

        keywordService.createKeywordWithNormalizations(
                "예약가능", MbtiAxis.J,
                List.of("예약가능", "예약 가능", "예약")
        );

        /* ================= P ================= */
        keywordService.createKeywordWithNormalizations(
                "자유로운", MbtiAxis.P,
                List.of(
                        "자유로운", "프리한", "자유",
                        "유연한", "선택많은",
                        "산책하기좋은"
                )
        );

        keywordService.createKeywordWithNormalizations(
                "즉흥적인", MbtiAxis.P,
                List.of("즉흥적인", "즉흥적", "즉흥")
        );


        seedHistoryRepository.save(new AppSeedHistory("KEYWORD"));
    }

    private void seed(String name, MbtiAxis axis) {
        if (keywordRepository.existsByName(name)) return;

        Keyword k = new Keyword(name, axis);
        keywordRepository.save(k);
    }
}
