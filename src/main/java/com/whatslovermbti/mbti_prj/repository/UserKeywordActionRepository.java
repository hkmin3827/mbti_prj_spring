package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.entity.UserKeywordAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserKeywordActionRepository
        extends JpaRepository<UserKeywordAction, Long> {

    @Query("""
    select coalesce(sum(a.count), 0)
    from UserKeywordAction a
    where a.user = :user
      and a.keyword = :keyword
      and a.actionType = :type
      and a.mbtiContext = :context
""")
    int countBy(
            User user,
            Keyword keyword,
            ActionType type,
            MbtiContext context
    );
}
