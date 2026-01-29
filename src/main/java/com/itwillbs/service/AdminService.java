package com.itwillbs.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.ReportVO;
import com.itwillbs.entity.Notice;
import com.itwillbs.entity.Report;
import com.itwillbs.entity.enumtype.ReportStatus;
import com.itwillbs.repository.NoticeRepository;
import com.itwillbs.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final NoticeRepository noticeRepository;
	private final ReportRepository reportRepository;
	
	
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
}
