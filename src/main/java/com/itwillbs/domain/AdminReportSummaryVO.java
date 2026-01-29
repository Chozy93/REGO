package com.itwillbs.domain;

import java.util.List;

import com.itwillbs.entity.Report;

import lombok.Getter;

@Getter
public class AdminReportSummaryVO {

    private final List<AdminRecentReportItemVO> recentReports;

    public AdminReportSummaryVO(List<Report> reports) {
        this.recentReports = reports.stream()
            .map(AdminRecentReportItemVO::new)
            .toList();
    }
}
