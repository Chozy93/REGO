package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.dto.AdminInquiryListDTO;
import com.itwillbs.domain.AdminInquiryDetailVO;
import com.itwillbs.domain.AdminInquirySearchConditionVO;

@Mapper
public interface AdminInquiryMapper {

    // 목록
    int countInquiries(AdminInquirySearchConditionVO condition);
    List<AdminInquiryListDTO> findInquiries(AdminInquirySearchConditionVO condition);

    // 상세
    AdminInquiryDetailVO selectInquiryDetail(@Param("id") Long id);

    // 답변 등록
    void updateInquiryAnswer(
        @Param("id") Long id,
        @Param("answerContent") String answerContent,
        @Param("adminId") Long adminId
    );

    // 카운트
    int countAll();
    int countWaiting();
    int countDone();
}
