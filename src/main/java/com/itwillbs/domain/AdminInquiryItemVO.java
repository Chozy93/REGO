package com.itwillbs.domain;

import java.time.LocalDateTime;

import com.itwillbs.dto.AdminInquiryListDTO;

import lombok.Getter;

@Getter
public class AdminInquiryItemVO {

    private Long id;
    private String title;
    private String inquiryType;
    private String status;
    private LocalDateTime createdAt;

    public static AdminInquiryItemVO from(AdminInquiryListDTO dto) {
        AdminInquiryItemVO vo = new AdminInquiryItemVO();
        vo.id = dto.getId();
        vo.title = dto.getTitle();
        vo.inquiryType = dto.getInquiryType();
        vo.status = dto.getStatus();
        vo.createdAt = dto.getCreatedAt();
        return vo;
    }
}
