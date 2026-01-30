package com.itwillbs.view.condition;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportConditionVO {

    /* =========================
       신고 대상
    ========================= */
    private String targetTypeCode;   // PRODUCT, USER, REVIEW ...
    private Long targetId;

    /* =========================
       신고 사유
    ========================= */
    private String reasonCode;       // SPAM, FRAUD, ILLEGAL, ABUSE, ETC
    private String detail;           // 상세 내용 (필수/선택은 서비스에서 판단)
}
