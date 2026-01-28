package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.view.MyOrderSelectViewVO;

@Mapper
public interface CustomerMapper {
	
	 List<MyOrderSelectViewVO> selectMyOrdersForInquiry(Long userId);
	
}
