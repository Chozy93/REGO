package com.itwillbs.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminInquiryDetailVO {

    private Long id;
    private String inquiryType;
    private String title;
    private String content;

    private String status; // WAITING / DONE

    private LocalDateTime createdAt;

    private String answerContent;
    private LocalDateTime answeredAt;
}
