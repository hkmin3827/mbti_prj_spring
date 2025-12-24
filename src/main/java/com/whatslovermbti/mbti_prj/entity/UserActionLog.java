package com.whatslovermbti.mbti_prj.entity;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_action_user", columnList = "user_id"),
                @Index(name = "idx_action_place", columnList = "place_id"),
                @Index(name = "idx_action_place_type", columnList = "place_id, actionType"),
                @Index(name = "idx_action_mbti", columnList = "target_mbti"),
                @Index(name = "idx_action_created", columnList = "createdAt")
        }
)
public class UserActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActionType actionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_mbti", nullable = false, length = 10)
    private MbtiContext targetMbti;


    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static UserActionLog of(
            User user,
            Place place,
            ActionType actionType,
            MbtiContext targetMbti
    ) {
        UserActionLog log = new UserActionLog();
        log.user = user;
        log.place = place;
        log.actionType = actionType;
        log.createdAt = LocalDateTime.now();
        log.targetMbti = targetMbti;
        return log;
    }
}
