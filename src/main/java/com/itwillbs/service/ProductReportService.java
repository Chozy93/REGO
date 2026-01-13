package com.itwillbs.service;

import com.itwillbs.common.exception.DuplicateProductReportException;
import com.itwillbs.domain.ReportVO;
import com.itwillbs.entity.Report;
import com.itwillbs.entity.User;
import com.itwillbs.entity.enumtype.ReportTargetType;
import com.itwillbs.repository.ReportRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductReportService {

    private final ReportRepository reportRepository;

    /* =========================
       🚨 신고 접수 (DETAIL01_REPORT_DUP)
    ========================= */
    @Transactional
    public void reportProduct(Long productId, ReportVO reportVO, User loginUser) {

        if (productId == null) {
            throw new IllegalArgumentException("상품 ID가 없습니다.");
        }
        if (loginUser == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        // ✅ 중복 신고 방지
        boolean alreadyReported =
            reportRepository.existsByTargetTypeAndTargetIdAndReporter_UserId(
                ReportTargetType.PRODUCT,
                productId,
                loginUser.getUserId()
            );

        if (alreadyReported) {
            throw new DuplicateProductReportException();
        }

        ReportVO fixedVO = new ReportVO(
            "PRODUCT",
            productId,
            reportVO.getReason(),
            reportVO.getDetail()
        );

        Report report = new Report(loginUser, fixedVO);
        reportRepository.save(report);
    }

    /* =========================
       🔍 신고 상태 조회 (DETAIL01_REPORT_STATUS)
    ========================= */
    @Transactional(readOnly = true)
    public boolean isAlreadyReported(Long productId, User loginUser) {

        if (productId == null || loginUser == null) {
            return false;
        }

        return reportRepository.existsByTargetTypeAndTargetIdAndReporter_UserId(
            ReportTargetType.PRODUCT,
            productId,
            loginUser.getUserId()
        );
    }
}
