package com.whatslovermbti.mbti_prj.service;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.entity.UserKeywordPreference;
import com.whatslovermbti.mbti_prj.repository.UserKeywordPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserKeywordActionService {

    private final UserKeywordPreferenceRepository preferenceRepository;

    @Transactional
    public void onKeywordAction(
            User user,
            Keyword keyword,
            ActionType type
    ) {
        int delta = switch (type) {
            case CLICK -> 1;
            case LIKE  -> 5;
            case SAVE  -> 10;
        };

        UserKeywordPreference pref =
                preferenceRepository.findByUserAndKeyword(user, keyword)
                        .orElseGet(() -> {
                            UserKeywordPreference p = new UserKeywordPreference();
                            p.setUser(user);
                            p.setKeyword(keyword);
                            p.setScore(0);
                            return p;
                        });

        pref.setScore(
                Math.min(pref.getScore() + delta, 100)
        );

        preferenceRepository.save(pref);
    }
}

