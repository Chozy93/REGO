package com.itwillbs.service;

import org.springframework.stereotype.Service;

import com.itwillbs.domain.AdminReportSummaryVO;
import com.itwillbs.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminReportDashboardService {

    private final ReportRepository reportRepository;

    

    public AdminReportSummaryVO getRecentReports() {
        return new AdminReportSummaryVO(
            reportRepository.findTop3ByOrderByCreatedAtDesc()
        );
    }
}
