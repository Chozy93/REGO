package com.itwillbs.domain;

import java.time.format.DateTimeFormatter;

import com.itwillbs.entity.Report;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReportVO {

    private final Long reportId;
    private final Long reporterId;

    /* enum 제거 */
    private final String targetTypeCode;
    private final String targetTypeLabel;

    private final Long targetId;

    private final String reason;
    private final String detail;

    private final String statusCode;
    private final String statusLabel;

    private final String createdAt;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /* =========================
       Entity → VO
    ========================= */
    public ReportVO(Report entity) {
        this.reportId = entity.getReportId();
        this.reporterId = entity.getReporter().getUserId();

        this.targetTypeCode = entity.getTargetType().name();
        this.targetTypeLabel = entity.getTargetType().getLabel();

        this.targetId = entity.getTargetId();
        this.reason = entity.getReason();
        this.detail = entity.getDetail();

        this.statusCode = entity.getStatus().name();
        this.statusLabel = entity.getStatus().getLabel();

        this.createdAt = entity.getCreatedAt().format(FORMATTER);
    }

    /* =========================
       등록용
    ========================= */
    public ReportVO(
            String targetTypeCode,
            Long targetId,
            String reason,
            String detail
    ) {
        this.reportId = null;
        this.reporterId = null;

        this.targetTypeCode = targetTypeCode;
        this.targetTypeLabel = "";

        this.targetId = targetId;
        this.reason = reason;
        this.detail = detail;

        this.statusCode = "";
        this.statusLabel = "";

        this.createdAt = "";
    }
}
