package com.itwillbs.dto;

import java.util.List;

import lombok.Data;

@Data
public class OpenBankingUserInfoResponseDTO {
	private String api_tran_id;
    private String rsp_code;
    private String rsp_message;
    private String user_seq_no;
    private String user_ci;
    private String user_name; // 사용자 실명
    
    private List<BankAccountDTO> res_list; // 등록된 계좌 목록

    @Data
    public static class BankAccountDTO {
        private String fintech_use_num;     // ★ 핀테크이용번호 (결제/송금 시 필수)
        private String account_alias;       // 계좌 별명
        private String bank_code_std;       // 은행 표준 코드
        private String bank_code_sub;       // 은행 점포 코드
        private String bank_name;           // ★ 은행 이름 (예: 신한은행)
        private String account_num_masked;  // ★ 마스킹된 계좌번호
        private String account_holder_name; // ★ 예금주 성명
        private String account_type;        // 계좌 구분 (1:수시입출금, 2:예적금 등)
    }
}
