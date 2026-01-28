package com.itwillbs.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

@Getter
public class AdminDashboardPageVO {

    private final String today;
    private final AdminMemberSummaryVO memberSummary;
    private final AdminProductSummaryVO productSummary;
    private final AdminReportSummaryVO reportSummary;

    public AdminDashboardPageVO(
        AdminMemberSummaryVO memberSummary,
        AdminProductSummaryVO productSummary,
        AdminReportSummaryVO reportSummary
    ) {
        this.today = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 (E)"));

        this.memberSummary = memberSummary;
        this.productSummary = productSummary;
        this.reportSummary = reportSummary;
    }
}
