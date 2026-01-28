package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.AdminInquiryDetailVO;
import com.itwillbs.domain.AdminInquiryItemVO;
import com.itwillbs.domain.AdminInquiryListPageVO;
import com.itwillbs.domain.AdminInquirySearchConditionVO;
import com.itwillbs.mapper.AdminInquiryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminInquiryService {

    private final AdminInquiryMapper mapper;

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
    public void answerInquiry(Long id, String answerContent) {
        mapper.updateInquiryAnswer(id, answerContent);
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
