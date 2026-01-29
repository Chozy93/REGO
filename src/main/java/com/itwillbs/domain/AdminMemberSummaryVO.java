package com.itwillbs.domain;

import com.itwillbs.dto.AdminMemberSummaryDTO;

import lombok.Getter;

@Getter
public class AdminMemberSummaryVO {

    private final int totalMemberCount;
    private final String totalIncreasePercent;
    private final boolean isTotalIncrease;

    private final int todaySignupCount;
    private final String todayIncreasePercent;
    private final boolean isTodayIncrease;

    public AdminMemberSummaryVO(AdminMemberSummaryDTO dto) {

        this.totalMemberCount = (int) dto.getTotalMemberCount();
        this.todaySignupCount = (int) dto.getTodaySignupCount();

        // 지난달 대비 총 회원 수
        PercentResult totalResult =
            calculate(dto.getTotalMemberCount(), dto.getLastMonthMemberCount());
        this.totalIncreasePercent = totalResult.percent();
        this.isTotalIncrease = totalResult.isIncrease();

        // 전일 대비 오늘 가입자
        PercentResult todayResult =
            calculate(dto.getTodaySignupCount(), dto.getYesterdaySignupCount());
        this.todayIncreasePercent = todayResult.percent();
        this.isTodayIncrease = todayResult.isIncrease();
    }

    private PercentResult calculate(long current, long base) {
        if (base <= 0) {
            return new PercentResult("0", false);
        }

        double value = ((double)(current - base) / base) * 100;
        String percent = String.format("%.0f", value);

        return new PercentResult(percent, value > 0);
    }

    private record PercentResult(String percent, boolean isIncrease) {}
}

