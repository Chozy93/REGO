package com.itwillbs.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.dto.MonthlyCountDTO;
import com.itwillbs.dto.OrderListResponseDTO;
import com.itwillbs.entity.Notice;
import com.itwillbs.entity.Report;
import com.itwillbs.entity.User;
import com.itwillbs.entity.enumtype.EscrowStatus;
import com.itwillbs.entity.enumtype.ProductSalesStatus;
import com.itwillbs.entity.enumtype.ReportStatus;
import com.itwillbs.entity.enumtype.UserRole;
import com.itwillbs.entity.enumtype.UserStatus;
import com.itwillbs.repository.NoticeRepository;
import com.itwillbs.repository.OrderRepository;
import com.itwillbs.repository.ProductRepository;
import com.itwillbs.repository.ReportRepository;
import com.itwillbs.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final NoticeRepository noticeRepository;
	private final ReportRepository reportRepository;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	
	
	/**
     * 유저 권한 변경 로직
     */
    @Transactional
    public void updateUserRole(Long userId, UserRole role) {
        // 1. 해당 유저가 존재하는지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID: " + userId));

        // 2. 권한 업데이트 (더티 체킹으로 인해 트랜잭션 종료 시 자동 반영)
        user.updateRole(role);
        
        // (선택 사항) 로그 기록 등 추가 로직 수행
        // log.info("User {}'s role changed to {}", userId, role);
    }
	
    
	/**
     * 유저 상태(banned, active) 변경 로직
     */
    
    @Transactional
    public void updateUserStatus(Long userId, UserStatus status) {
        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. ID: " + userId));

        // 2. 상태 변경 (JPA 더티 체킹으로 자동 저장)
        user.updateStatus(status);
        
        // 로그 남기기 (선택사항)
        // log.info("User {} status changed to {}", userId, status);
    }
    
    
	
	// 상단 통계 데이터 가져오기
    public Map<String, Long> getNoticeStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", noticeRepository.count());
        stats.put("active", noticeRepository.countByIsActive(true));
        stats.put("hidden", noticeRepository.countByIsActive(false));
        return stats;
    }

    // 관리자용 전체 리스트 조회
    public Page<Notice> getAdminNoticeList(Pageable pageable) {
        return noticeRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    
    
    @Transactional
    public void updateStatus(Long id, boolean status) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
        
        if (status) {
            notice.activate(); // isActive = true
        } else {
            notice.deactivate(); // isActive = false
        }
        // @Transactional이 걸려있으므로 save()를 명시하지 않아도 더티 체킹으로 업데이트됩니다.
    }
    
    
    
    // -------------------- 신고하기 관리 페이지
	// 상단 통계 데이터 가져오기
    public Map<String, Long> getReportStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", reportRepository.count());
        stats.put("pending", reportRepository.countByStatus(ReportStatus.PENDING));
        stats.put("done", reportRepository.countByStatus(ReportStatus.DONE));
        return stats;
    }
    
    
    // 신고하기 리스트 가져오기
    public Page<Report> getReportList(Pageable pageable) {
       
        return reportRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

 

    // 신고하기 상태 변환
    @Transactional
    public void updateReportStatus(Long reportId, String newStatus) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("해당 신고가 존재하지 않습니다."));
        
        // String을 Enum으로 변환하거나 직접 문자열 세팅
        report.markDone();
    }
    
    
    
    // --- order 관리
    // 주문 내역 가져오기
    // 목록 조회 로직 (기존 구현 내용)
    public Page<OrderListResponseDTO> findAllOrders(String status, String search, Pageable pageable) {
        EscrowStatus escrowStatus = null;
        if (status != null && !status.isEmpty() && !status.equals("전체 상태")) {
            try {
                escrowStatus = EscrowStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                escrowStatus = null;
            }
        }
        return orderRepository.searchOrders(escrowStatus, search, pageable);
    }
 // 총 거래 수
    public long getTotalCount() {
        return orderRepository.count();
    }

    // 분쟁 접수 수 (예시로 CANCELLED나 별도의 DISPUTE 상태가 있다면 그것을 사용)
    public long getDisputeCount() {
        // 실제 프로젝트의 분쟁 상태 Enum값을 넣으세요.
        // 여기서는 예시로 CANCELLED를 사용합니다.
        return orderRepository.countByEscrowStatus(EscrowStatus.CANCELLED);
    }

    
    
    // *********
    // ----------- 통계 ----
    // *******
    /**
     * [공통 로직] 
     * DB에서 가져온 월별 통계(데이터가 있는 달만 존재)를 
     * 1월~12월까지 0이 채워진 12개의 리스트로 변환합니다.
     */
    private List<Long> convertToMonthlyList(List<MonthlyCountDTO> results) {
        Long[] counts = new Long[12];
        Arrays.fill(counts, 0L);

        if (results != null) {
            for (MonthlyCountDTO dto : results) {
                // 월(month)은 1~12이므로 배열 인덱스(0~11)를 위해 -1 해줌
                int monthIndex = dto.getMonth() - 1; 
                if (monthIndex >= 0 && monthIndex < 12) {
                    counts[monthIndex] = dto.getCount();
                }
            }
        }
        return Arrays.asList(counts);
    }
    
 // 1. 회원 가입 추이
    public List<Long> getUserCountsForChart() {
        return convertToMonthlyList(userRepository.getMonthlyUserCounts());
    }

    // 2. 상품 등록 추이
    public List<Long> getProductCountsForChart() {
        return convertToMonthlyList(productRepository.getMonthlyProductCounts());
    }

    // 3. 거래 완료 추이 (updatedAt 기준)
    public List<Long> getOrderCountsForChart() {
    	// 호출 시 Enum 값을 직접 전달
        List<MonthlyCountDTO> results = productRepository.getMonthlyOrderCounts(ProductSalesStatus.SOLD);
        return convertToMonthlyList(results);
    }
    
    
  
}
