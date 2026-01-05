package com.itwillbs.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.itwillbs.dto.MainProductListDTO;

@Mapper
public interface MainProductMapper {

    // 최근 등록 상품
    List<MainProductListDTO> selectRecentProducts();

    // 인기 상품
    List<MainProductListDTO> selectPopularProducts();
}
