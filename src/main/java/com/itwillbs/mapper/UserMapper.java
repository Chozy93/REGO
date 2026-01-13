package com.itwillbs.mapper;

import com.itwillbs.dto.SocialAccountDTO;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    // 1. 이미 연결된 소셜 계정이 있는지 확인
	SocialAccountDTO findSocialAccount(@Param("provider") String provider, @Param("providerId") String providerId);

    // 2. 소셜 계정 정보 저장
    void insertSocialAccount(SocialAccountDTO socialAccountDTO);
    
    void insertUser(Map<String, Object> userParams);
}