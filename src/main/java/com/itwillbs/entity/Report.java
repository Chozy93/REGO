package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import com.itwillbs.domain.ReportVO;
import com.itwillbs.entity.enumtype.ReportStatus;
import com.itwillbs.entity.enumtype.ReportTargetType;

@Entity
@Table(name = "reports")
@Getter
public class Report {

    /* =========================
       PK
    ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    /* =========================
       신고자
    ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "reporter_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_reports_reporter")
    )
    private User reporter;

    /* =========================
       신고 대상 (다형)
    ========================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private ReportTargetType targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    /* =========================
       신고 내용
    ========================= */
    @Column(name = "reason", length = 50, nullable = false)
    private String reason;

    @Column(name = "detail")
    private String detail;

    /* =========================
       처리 상태
    ========================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportStatus status;

    /* =========================
       날짜
    ========================= */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /* =========================
       JPA 전용 생성자
    ========================= */
    protected Report() {}
    /* =========================
    생성자 (VO → Entity)
 ========================= */
 public Report(User reporter, ReportVO vo) {
     this.reporter = reporter;

     this.targetType = ReportTargetType.valueOf(vo.getTargetTypeCode());
     this.targetId = vo.getTargetId();

     this.reason = vo.getReason();
     this.detail = vo.getDetail();

     this.status = ReportStatus.PENDING;
     this.createdAt = LocalDateTime.now();
 }


    /* =========================
       Entity → VO
    ========================= */
    public ReportVO toVO() {
        return new ReportVO(this);
    }

    /* =========================
       처리 완료
    ========================= */
    public void markDone() {
        this.status = ReportStatus.DONE;
    }
}
