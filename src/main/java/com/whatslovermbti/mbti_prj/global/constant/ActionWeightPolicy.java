package com.whatslovermbti.mbti_prj.global.constant;

import java.util.Arrays;

public enum ActionWeightPolicy {
    VIEW(ActionType.VIEW, 0.2),
    LIKE(ActionType.LIKE, 0.6),
    DISLIKE(ActionType.DISLIKE, -0.6),
    SAVE(ActionType.SAVE, 1.2),
    UNSAVE(ActionType.UNSAVE, -1.2);

    private final ActionType actionType;
    private final double weight;

    ActionWeightPolicy(ActionType actionType, double weight) {
        this.actionType = actionType;
        this.weight = weight;
    }

    public static double getWeight(ActionType actionType) {
        return Arrays.stream(values())
                .filter(p -> p.actionType == actionType)
                .findFirst()
                .orElseThrow()
                .weight;
    }
}