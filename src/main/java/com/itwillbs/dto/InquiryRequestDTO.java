package com.itwillbs.dto;

import com.itwillbs.entity.enumtype.InquiryType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InquiryRequestDTO {
	private InquiryType inquiryType; // Enum과 자동 매핑됩니다
    private String title;
    private String content;
    private Long orderId; // 결제/주문 관련 선택 시 넘어옴
}
