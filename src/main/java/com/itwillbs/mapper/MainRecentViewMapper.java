package com.itwillbs.mapper;

import com.itwillbs.view.MainProductCardVO;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainRecentViewMapper {

    List<MainProductCardVO> selectRecentViewProducts(List<Long> ids);
}
