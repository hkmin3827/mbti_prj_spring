package com.whatslovermbti.mbti_prj.application.keyword;

import com.whatslovermbti.mbti_prj.domain.keyword.entity.Keyword;
import com.whatslovermbti.mbti_prj.domain.keyword.entity.MbtiKeywordWeight;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import com.whatslovermbti.mbti_prj.domain.userKeywordPreference.UserKeywordPreference;
import com.whatslovermbti.mbti_prj.domain.keyword.repository.MbtiKeywordWeightRepository;
import com.whatslovermbti.mbti_prj.domain.userKeywordPreference.UserKeywordPreferenceRepository;
import com.whatslovermbti.mbti_prj.global.util.MbtiAxisUtil;
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

    @Test
    void MBTI_축에_해당하는_키워드_가중치가_합산된다() {
        MbtiKeywordWeight w1 = mock(MbtiKeywordWeight.class);
        MbtiKeywordWeight w2 = mock(MbtiKeywordWeight.class);

        when(w1.getWeight()).thenReturn(10);
        when(w2.getWeight()).thenReturn(5);

        var keyword = mock(Keyword.class);
        when(keyword.getName()).thenReturn("감성");

        when(w1.getKeyword()).thenReturn(keyword);
        when(w2.getKeyword()).thenReturn(keyword);

        when(mbtiKeywordWeightRepository.findAllByAxes(anySet()))
                .thenReturn(List.of(w1, w2));

        try (MockedStatic<MbtiAxisUtil> mocked = mockStatic(MbtiAxisUtil.class)) {
            mocked.when(() -> MbtiAxisUtil.parseAxes("ENFP"))
                    .thenReturn(Map.of(
                            "E", 1,
                            "N", 1,
                            "F", 1,
                            "P", 1
                    ));


            Map<String, Integer> result =
                    aggregator.getMbtiKeywordWeightMapByName("ENFP");

            assertThat(result.get("감성")).isEqualTo(15);
        }
    }

    @Test
    void 유저_행동_키워드_점수가_이름_기준으로_합산된다() {
        User user = mock(User.class);

        UserKeywordPreference p1 = mock(UserKeywordPreference.class);
        UserKeywordPreference p2 = mock(UserKeywordPreference.class);

        var keyword = mock(Keyword.class);
        when(keyword.getName()).thenReturn("조용한");

        when(p1.getKeyword()).thenReturn(keyword);
        when(p2.getKeyword()).thenReturn(keyword);

        when(p1.getScore()).thenReturn(2.5);
        when(p2.getScore()).thenReturn(1.5);

        when(userKeywordPreferenceRepository.findAllByUser(user))
                .thenReturn(List.of(p1, p2));

        Map<String, Double> result =
                aggregator.getUserKeywordPreferenceMapByName(user);

        assertThat(result.get("조용한")).isEqualTo(4.0);
    }

    @Test
    void MBTI_가중치와_유저_행동_가중치가_합산된다() {
        User user = mock(User.class);

        KeywordWeightAggregator spy = spy(aggregator);

        doReturn(Map.of("분위기좋은", 20))
                .when(spy).getMbtiKeywordWeightMapByName("INFJ");

        doReturn(Map.of("분위기좋은", 7.5))
                .when(spy).getUserKeywordPreferenceMapByName(user);

        Map<String, Double> result =
                spy.getCombinedKeywordWeightMapByName(user, "INFJ");

        assertThat(result.get("분위기좋은")).isEqualTo(27.5);
    }

    @Test
    void 유저_행동이_없어도_MBTI_가중치는_유지된다() {
        User user = mock(User.class);

        KeywordWeightAggregator spy = spy(aggregator);

        doReturn(Map.of("힐링", 12))
                .when(spy).getMbtiKeywordWeightMapByName("ISFP");

        doReturn(Map.of())
                .when(spy).getUserKeywordPreferenceMapByName(user);

        Map<String, Double> result =
                spy.getCombinedKeywordWeightMapByName(user, "ISFP");

        assertThat(result)
                .containsEntry("힐링", 12.0);
    }
}
