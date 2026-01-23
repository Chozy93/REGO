package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.dto.SellerShopProductListDTO;
import com.itwillbs.view.SellerShopSellerProfileVO;

@Mapper
public interface SellerShopMapper {

    /**
     * 판매자 상점 - 판매중 상품 목록 조회
     */
    List<SellerShopProductListDTO> selectSellerProducts(
            @Param("sellerId") Long sellerId
    );
    
    SellerShopSellerProfileVO selectSellerProfile(
            @Param("sellerId") Long sellerId
    );

}
