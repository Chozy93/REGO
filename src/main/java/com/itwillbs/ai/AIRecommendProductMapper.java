package com.itwillbs.ai;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AIRecommendProductMapper {

    List<AIRecommendProductDTO> selectAIProducts();
}
