package com.itwillbs.entity.enumtype;

public enum SocialProvider {

    GOOGLE("구글"),
    KAKAO("카카오"),
    NAVER("네이버");

    private final String label;

    SocialProvider(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
