package com.itwillbs.domain.user;

import com.itwillbs.domain.ReportVO;
import com.itwillbs.entity.Report;
import com.itwillbs.entity.User;

public class ReportFactory {

    private ReportFactory() {
        // static factory only
    }

    /**
     * 상품 신고 생성
     */
    public static Report createProductReport(
            User reporter,
            Long productId,
            String reasonCode
    ) {
        ReportVO reportVO = new ReportVO(
            "PRODUCT",
            productId,
            reasonCode,
            null
        );

        return new Report(reporter, reportVO);
    }
}
