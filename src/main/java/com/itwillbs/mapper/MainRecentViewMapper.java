package com.itwillbs.mapper;

import com.itwillbs.dto.MainRecentViewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainRecentViewMapper {

    List<MainRecentViewDTO> selectRecentProducts(List<Long> ids);
}
