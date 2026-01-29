package com.itwillbs.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.entity.Notice;
import com.itwillbs.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final NoticeRepository noticeRepository;
	
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
}
