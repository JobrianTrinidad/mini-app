package com.jo.application.data.entity;

public enum IntervalType {
    S("Scheduled"), R ("Repair"), UR ("Upgrade");

    private final String name;

    IntervalType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
