package com.adhd.ad_hell.domain.notification.command.entity;

public enum YnType {
    Y,
    N;

    public static YnType yes() {
        return Y;
    }

    public static YnType no() {
        return N;
    }

    public boolean isYes() {
        return this == Y;
    }
}