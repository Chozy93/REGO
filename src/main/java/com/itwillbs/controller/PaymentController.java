package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


// 결제 제어 컨트롤러
@Controller
public class PaymentController {

	// 바로 결제 페이지 이동
    @GetMapping("/direct-payments")
    public String directPayments() {
        return "payment/direct-pay"; 
    }
	
}
