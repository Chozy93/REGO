package com.itwillbs.dto;

import lombok.Getter;

@Getter
public class AdminMemberSummaryDTO {

    private final long totalMemberCount;
    private final long lastMonthMemberCount;
    private final long todaySignupCount;
    private final long yesterdaySignupCount;

    public AdminMemberSummaryDTO(
        long totalMemberCount,
        long lastMonthMemberCount,
        long todaySignupCount,
        long yesterdaySignupCount
    ) {
        this.totalMemberCount = totalMemberCount;
        this.lastMonthMemberCount = lastMonthMemberCount;
        this.todaySignupCount = todaySignupCount;
        this.yesterdaySignupCount = yesterdaySignupCount;
    }
}
