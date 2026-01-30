package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.AdminInquiryDetailVO;
import com.itwillbs.domain.AdminInquiryItemVO;
import com.itwillbs.domain.AdminInquiryListPageVO;
import com.itwillbs.domain.AdminInquirySearchConditionVO;
import com.itwillbs.entity.User;
import com.itwillbs.mapper.AdminInquiryMapper;
import com.itwillbs.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminInquiryService {

    private final AdminInquiryMapper mapper;
    private final UserRepository userRepository;

    // 📌 문의 목록
    public AdminInquiryListPageVO getInquiryPage(AdminInquirySearchConditionVO condition) {

        int totalCount = mapper.countInquiries(condition);

        List<AdminInquiryItemVO> list =
                mapper.findInquiries(condition)
                      .stream()
                      .map(AdminInquiryItemVO::from)
                      .toList();

        return new AdminInquiryListPageVO(
                list,
                condition.getPage(),
                totalCount,
                condition.getSize()
        );
    }

    // 📌 문의 상세
    public AdminInquiryDetailVO getInquiryDetail(Long id) {
        return mapper.selectInquiryDetail(id);
    }

    // 📌 문의 답변 등록
    @Transactional
    public void answerInquiry(Long id, String answerContent, String adminLoginId) {
    	// 1. 로그인 아이디로 관리자 정보(특히 PK인 id) 조회
        // (예: UserMapper나 UserRepository를 통해 admin의 Long id를 가져와야 함)
    	// 1. 로그인한 관리자의 username으로 User 엔티티 조회
        User admin = userRepository.findByEmail(adminLoginId)
                .orElseThrow(() -> new IllegalArgumentException("관리자 정보를 찾을 수 없습니다."));
    	
     // 로그를 찍어서 어떤 값이 들어오는지 확인해보세요
        System.out.println("조회하려는 관리자 ID: " + adminLoginId);
        
        mapper.updateInquiryAnswer(id, answerContent, admin.getUserId());
        
        
        
        
    }

    // 📌 대시보드용 카운트
    public int getTotalCount() {
        return mapper.countAll();
    }

    public int getWaitingCount() {
        return mapper.countWaiting();
    }

    public int getDoneCount() {
        return mapper.countDone();
    }
}
