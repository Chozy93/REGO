package com.itwillbs.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportDetailResponseDTO {
    private String reasonLabel;
    private String detail;
    private String reporter;
    private String target;

}
