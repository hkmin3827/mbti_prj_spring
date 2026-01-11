package com.whatslovermbti.mbti_prj.service;

import com.whatslovermbti.mbti_prj.entity.MbtiKeywordWeight;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.entity.UserKeywordPreference;
import com.whatslovermbti.mbti_prj.repository.MbtiKeywordWeightRepository;
import com.whatslovermbti.mbti_prj.repository.UserKeywordPreferenceRepository;
import com.whatslovermbti.mbti_prj.service.keyword.KeywordWeightAggregator;
import com.whatslovermbti.mbti_prj.util.MbtiAxisUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeywordWeightAggregatorTest {

    @Mock
    MbtiKeywordWeightRepository mbtiKeywordWeightRepository;

    @Mock
    UserKeywordPreferenceRepository userKeywordPreferenceRepository;

    @InjectMocks
    KeywordWeightAggregator aggregator;

    /**
     * 1️⃣ MBTI 기준 키워드 기본 가중치 집계
     */
    @Test
    void MBTI_축에_해당하는_키워드_가중치가_합산된다() {
        // given
        MbtiKeywordWeight w1 = mock(MbtiKeywordWeight.class);
        MbtiKeywordWeight w2 = mock(MbtiKeywordWeight.class);

        when(w1.getWeight()).thenReturn(10);
        when(w2.getWeight()).thenReturn(5);

        var keyword = mock(com.whatslovermbti.mbti_prj.entity.Keyword.class);
        when(keyword.getName()).thenReturn("감성");

        when(w1.getKeyword()).thenReturn(keyword);
        when(w2.getKeyword()).thenReturn(keyword);

        when(mbtiKeywordWeightRepository.findAllByAxes(any(Set.class)))
                .thenReturn(List.of(w1, w2));

        // static 메서드 mock (핵심 포인트)
        try (MockedStatic<MbtiAxisUtil> mocked = mockStatic(MbtiAxisUtil.class)) {
            mocked.when(() -> MbtiAxisUtil.parseAxes("ENFP"))
                    .thenReturn(Map.of(
                            "E", 1,
                            "N", 1,
                            "F", 1,
                            "P", 1
                    ));


            // when
            Map<String, Integer> result =
                    aggregator.getMbtiKeywordWeightMapByName("ENFP");

            // then
            assertThat(result.get("감성")).isEqualTo(15);
        }
    }

    /**
     * 2️⃣ 유저 행동 기반 키워드 선호 점수 집계
     */
    @Test
    void 유저_행동_키워드_점수가_이름_기준으로_합산된다() {
        // given
        User user = mock(User.class);

        UserKeywordPreference p1 = mock(UserKeywordPreference.class);
        UserKeywordPreference p2 = mock(UserKeywordPreference.class);

        var keyword = mock(com.whatslovermbti.mbti_prj.entity.Keyword.class);
        when(keyword.getName()).thenReturn("조용한");

        when(p1.getKeyword()).thenReturn(keyword);
        when(p2.getKeyword()).thenReturn(keyword);

        when(p1.getScore()).thenReturn(2.5);
        when(p2.getScore()).thenReturn(1.5);

        when(userKeywordPreferenceRepository.findAllByUser(user))
                .thenReturn(List.of(p1, p2));

        // when
        Map<String, Double> result =
                aggregator.getUserKeywordPreferenceMapByName(user);

        // then
        assertThat(result.get("조용한")).isEqualTo(4.0);
    }

    /**
     * 3️⃣ MBTI 기본 가중치 + 유저 행동 가중치 병합
     */
    @Test
    void MBTI_가중치와_유저_행동_가중치가_합산된다() {
        // given
        User user = mock(User.class);

        KeywordWeightAggregator spy = spy(aggregator);

        doReturn(Map.of("분위기좋은", 20))
                .when(spy).getMbtiKeywordWeightMapByName("INFJ");

        doReturn(Map.of("분위기좋은", 7.5))
                .when(spy).getUserKeywordPreferenceMapByName(user);

        // when
        Map<String, Double> result =
                spy.getCombinedKeywordWeightMapByName(user, "INFJ");

        // then
        assertThat(result.get("분위기좋은")).isEqualTo(27.5);
    }

    /**
     * 4️⃣ 유저 행동이 없어도 MBTI 단독 계산은 가능해야 한다
     */
    @Test
    void 유저_행동이_없어도_MBTI_가중치는_유지된다() {
        // given
        User user = mock(User.class);

        KeywordWeightAggregator spy = spy(aggregator);

        doReturn(Map.of("힐링", 12))
                .when(spy).getMbtiKeywordWeightMapByName("ISFP");

        doReturn(Map.of())
                .when(spy).getUserKeywordPreferenceMapByName(user);

        // when
        Map<String, Double> result =
                spy.getCombinedKeywordWeightMapByName(user, "ISFP");

        // then
        assertThat(result)
                .containsEntry("힐링", 12.0);
    }
}
