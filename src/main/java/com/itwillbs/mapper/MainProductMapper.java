package com.itwillbs.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.itwillbs.dto.MainProductDTO;

@Mapper
public interface MainProductMapper {

    List<MainProductDTO> selectMainProductList();

}
