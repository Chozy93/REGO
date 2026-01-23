package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.dto.MainProductListDTO;

@Mapper
public interface MainProductMapper {

    // 최근 등록 상품 (기본 최신순)
    List<MainProductListDTO> selectRecentProducts(
            @Param("userId") Long userId
    );

    // 인기 상품
    List<MainProductListDTO> selectPopularProducts(
    	    @Param("userId") Long userId,
    	    @Param("limit") int limit,
    	    @Param("sort") String sort,    // 추가
    	    @Param("region") String region // 추가
    	);


    // 최근 등록 상품 (정렬: 최신순 / 인기순)
    List<MainProductListDTO> selectRecentProductsWithSort(
            @Param("userId") Long userId,
            @Param("sort") String sort,
            @Param("region") String region // region 추가
    );
}
