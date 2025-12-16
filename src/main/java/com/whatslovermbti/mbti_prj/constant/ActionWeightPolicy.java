package com.whatslovermbti.mbti_prj.constant;

import java.util.Arrays;

public enum ActionWeightPolicy {

    VIEW(ActionType.VIEW, 1),
    LIKE(ActionType.LIKE, 5),
    DISLIKE(ActionType.DISLIKE, -5),
    SAVE(ActionType.SAVE, 10),
    UNSAVE(ActionType.UNSAVE, -10);

    private final ActionType actionType;
    private final int weight;

    ActionWeightPolicy(ActionType actionType, int weight) {
        this.actionType = actionType;
        this.weight = weight;
    }

    public static int getWeight(ActionType actionType) {
        return Arrays.stream(values())
                .filter(p -> p.actionType == actionType)
                .findFirst()
                .orElseThrow()
                .weight;
    }
}