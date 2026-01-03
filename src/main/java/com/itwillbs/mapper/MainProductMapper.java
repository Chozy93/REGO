package com.itwillbs.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.itwillbs.dto.MainProductListDTO;

@Mapper
public interface MainProductMapper {

    List<MainProductListDTO> selectMainProductList();

}
