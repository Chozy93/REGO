package com.itwillbs.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.itwillbs.dto.MyPageDTO;

@Mapper
public interface MypageMapper {
    MyPageDTO getMyPageInfo(@Param("email") String email);
}