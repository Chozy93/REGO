package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.dto.AiRecommendProductDTO;

@Mapper
public interface MainAiRecommendMapper {

    List<AiRecommendProductDTO> selectAiRecommendProductsByRecent(
        @Param("recentIds") List<Long> recentIds,
        @Param("userId") Long userId
    );
}