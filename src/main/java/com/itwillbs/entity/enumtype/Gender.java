package com.itwillbs.entity.enumtype;

public enum Gender {

    M("남"),
    F("여");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
