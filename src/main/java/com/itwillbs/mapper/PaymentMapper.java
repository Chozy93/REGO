package com.itwillbs.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.dto.WalletViewDTO;

@Mapper
public interface PaymentMapper {

	// 유저 id루 지갑+계좌 정보 조회
	WalletViewDTO selectWalletWithAccount(Long userId);
}
