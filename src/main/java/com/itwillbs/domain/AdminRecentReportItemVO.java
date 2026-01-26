package com.itwillbs.domain;

import java.time.Duration;
import java.time.LocalDateTime;

import com.itwillbs.entity.Report;
import com.itwillbs.entity.enumtype.ReportStatus;

import lombok.Getter;

@Getter
public class AdminRecentReportItemVO {

    private final String reason;
    private final String createdAt;
    private final String status;
    private final boolean isDone;

    public AdminRecentReportItemVO(Report report) {
        this.reason = report.getReason();
        this.createdAt = toRelativeTime(report.getCreatedAt());
        this.status = report.getStatus().getLabel();
        this.isDone = report.getStatus() == ReportStatus.DONE;
    }

    private String toRelativeTime(LocalDateTime time) {
        Duration duration = Duration.between(time, LocalDateTime.now());

        long minutes = duration.toMinutes();
        if (minutes < 1) return "방금 전";
        if (minutes < 60) return minutes + "분 전";

        long hours = duration.toHours();
        if (hours < 24) return hours + "시간 전";

        long days = duration.toDays();
        if (days < 30) return days + "일 전";

        return (days / 30) + "달 전";
    }
}
