package com.itwillbs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.NoticeVO;
import com.itwillbs.entity.Notice;
import com.itwillbs.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
	private final NoticeRepository noticeRepository;

	
	// -------- 공지사항 쓰기
    public void register(Long writerId, NoticeVO vo) {
        // 엔티티 생성 (우리가 만든 생성자 사용)
        Notice notice = new Notice(writerId, vo);
        notice.activate();
        // DB 저장
        noticeRepository.save(notice);
    }
    
 // ------- 공지사항 목록 조회
    @Transactional(readOnly = true)
    public Page<NoticeVO> getNoticeList(Pageable pageable) {
        return noticeRepository.findByIsActiveTrue(pageable).map(NoticeVO::new);
    }

    // -------- 공지사항 고정글 조회
    @Transactional(readOnly = true)
    public List<NoticeVO> getPinnedNotices() {
        return noticeRepository.findByIsPinnedTrueOrderByCreatedAtDesc()
                .stream().map(NoticeVO::new).toList();
    }
}
