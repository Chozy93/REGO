package com.itwillbs.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class AdminInquiryListDTO {

    private Long id;
    private String title;
    private String inquiryType;
    private String status;
    private LocalDateTime createdAt;
    private String content;
    private String answerContent;
}
