package com.itwillbs.service;

import com.itwillbs.common.exception.DuplicateProductReportException;
import com.itwillbs.domain.ReportVO;
import com.itwillbs.entity.Report;
import com.itwillbs.entity.User;
import com.itwillbs.entity.enumtype.ReportTargetType;
import com.itwillbs.repository.ReportRepository;
import com.itwillbs.repository.UserRepository;
import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.view.condition.ReportConditionVO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    /* =========================
       🚨 신고 접수 (DETAIL01_REPORT_SUBMIT + DUP)
       - Controller는 productId, reasonCode만 전달
       - 로그인 / VO 생성 / 중복 체크는 Service 책임
    ========================= */
    @Transactional
    public void reportProduct(ReportConditionVO condition) {

        // ===== 디버그 =====
        System.out.println("[REPORT] condition.targetId = " + condition.getTargetId());
        System.out.println("[REPORT] condition.targetTypeCode(before) = " + condition.getTargetTypeCode());

        if (condition.getTargetId() == null) {
            throw new IllegalArgumentException("상품 ID가 없습니다.");
        }

        User loginUser = SecurityUtil.getCurrentUser();
        if (loginUser == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // ===== targetType 확정 (핵심) =====
        ReportTargetType targetType = ReportTargetType.PRODUCT;
        condition.setTargetTypeCode(targetType.name());

        System.out.println("[REPORT] targetType(enum) = " + targetType);
        System.out.println("[REPORT] reporterId = " + loginUser.getUserId());

        // ===== 중복 신고 체크 =====
        boolean alreadyReported =
            reportRepository.existsByTargetTypeAndTargetIdAndReporterUserId(
                targetType,
                condition.getTargetId(),
                loginUser.getUserId()
            );

        System.out.println("[REPORT] alreadyReported = " + alreadyReported);

        if (alreadyReported) {
            throw new DuplicateProductReportException();
        }

        // ===== 저장 =====
        Report report = new Report(loginUser, condition);
        reportRepository.save(report);

        System.out.println("[REPORT] saved OK, reportId = " + report.getReportId());
    }


    /* =========================
    🔍 신고 상태 조회 (DETAIL01_REPORT_STATUS)
    - 상세 페이지 로딩 시 사용
    - 로그인 사용자 기준
 ========================= */
 @Transactional(readOnly = true)
 public boolean isAlreadyReported(Long productId, User loginUser) {

     if (productId == null || loginUser == null) {
         return false;
     }

     return reportRepository
         .existsByTargetTypeAndTargetIdAndReporterUserId(
        		 ReportTargetType.PRODUCT,
             productId,
             loginUser.getUserId()
         );
 }
 
 /* =========================
 🔍 신고 상태 조회 (ID 기반)
 - Service ↔ Service 전용
 - DETAIL 페이지에서 사용
========================= */
@Transactional(readOnly = true)
public boolean isAlreadyReported(Long productId, Long userId) {

  if (productId == null || userId == null) {
      return false;
  }

  return reportRepository
      .existsByTargetTypeAndTargetIdAndReporterUserId(
    		  ReportTargetType.PRODUCT,
          productId,
          userId
      );
}


}
