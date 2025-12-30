package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.entity.Report;
import com.itwillbs.entity.enumtype.ReportStatus;
import com.itwillbs.entity.enumtype.ReportTargetType;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReportVO {

    private final Long reportId;
    private final Long reporterId;

    private final ReportTargetType targetType;
    private final Long targetId;

    private final String reason;
    private final String detail;

    private final ReportStatus status;
    private final LocalDateTime createdAt;

    /* =========================
       Entity → VO
    ========================= */
    public ReportVO(Report entity) {
        this.reportId = entity.getReportId();
        this.reporterId = entity.getReporter().getUserId();
        this.targetType = entity.getTargetType();
        this.targetId = entity.getTargetId();
        this.reason = entity.getReason();
        this.detail = entity.getDetail();
        this.status = entity.getStatus();
        this.createdAt = entity.getCreatedAt();
    }

    /* =========================
       등록용
    ========================= */
    public ReportVO(
            ReportTargetType targetType,
            Long targetId,
            String reason,
            String detail
    ) {
        this.reportId = null;
        this.reporterId = null;
        this.targetType = targetType;
        this.targetId = targetId;
        this.reason = reason;
        this.detail = detail;
        this.status = null;
        this.createdAt = null;
    }
}
